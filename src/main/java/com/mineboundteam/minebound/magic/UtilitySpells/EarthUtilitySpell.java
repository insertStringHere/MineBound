package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider.UtilityToggle;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class EarthUtilitySpell extends PassiveSpellItem {
    public static IntValue TOLERANCE;
    public static BooleanValue USE_TAGS;

    public static final ResourceLocation VEIN_TAGS = new ResourceLocation(MineBound.MOD_ID, "vein_miner");

    private final EarthUtilitySpellConfig config;

    public EarthUtilitySpell(Properties properties, EarthUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.EARTH, SpellType.UTILITY);
        this.config = config;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            List<EarthUtilitySpell> equippedSpells = getEquippedSpellItemsOfType(EarthUtilitySpell.class, player);
            if (equippedSpells.size() > 0) {
                int manaReduction = 0;
                // Mob effect levels start at 0, so this starts at -1 to compensate for the off by 1
                int speedLevel = -1;
                for (EarthUtilitySpell s : equippedSpells) {
                    manaReduction += s.config.MANA_REDUCTION.get();
                    speedLevel += s.config.SPEED_LEVEL.get();
                }

                if (speedLevel > -1) {
                    int finalManaReduction = manaReduction;
                    player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("earth_utility", -finalManaReduction));
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 2, speedLevel, false, false));
                }
            } else {
                // Remove total mana reduction
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("earth_utility", 0));
            }
        }
    }

    protected final static List<Vec3> shifts = new ArrayList<>();
    protected static boolean doingSearch = false;

    static {
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++)
                    // stupid language
                    if (!(i == 0 && i == j && j == k))
                        shifts.add(new Vec3(i, j, k));
    }


    // We want this to run first because it'll cancel and rerun for each selected block
    // we don't want a bunch of potential subscribe events to run for no reason on the initial break.
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBreak(BlockEvent.BreakEvent event) {
        // While sending a blockbreak event from any other source, this should trigger.
        // But, since it breaks blocks too, there needs to be a check to prevent infinite recursion
        if (!doingSearch && event.getPlayer() instanceof ServerPlayer player && !player.isCreative()) {
            // Check if the player even wants to be vein mining. If not, we can safely leave the method.
            Optional<UtilityToggle> toggle = player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).resolve();
            if (toggle.isPresent() && !toggle.get().earth)
                return;

            EarthUtilitySpell spell = getHighestSpellItem(EarthUtilitySpell.class, player);
            Level level = player.level;
            BlockState block = event.getState();

            // If we're doing tag checking, make sure the block is vein-mineable 
            if (spell == null || (USE_TAGS.get() && !block.getTags().anyMatch(t -> t.location().equals(VEIN_TAGS))))
                return;

            // Simple breadth first search; could implement a quicker search using the block type as a heuristic
            LinkedList<BlockPosSearch> open = new LinkedList<>();
            LinkedList<BlockPosSearch> closed = new LinkedList<>();

            open.addLast(new BlockPosSearch(event.getPos(), 0));
            int minedBlocks = 0;

            BiFunction<BlockPos, Integer, Boolean> checkVals = (newPos, dist) -> {
                for (BlockPosSearch val : open)
                    if (val.loc.equals(newPos)) {
                        if (val.notFoundDist > dist)
                            val.notFoundDist = dist;
                        return true;
                    }
                for (BlockPosSearch val : closed)
                    if (val.loc.equals(newPos))
                        return true;

                return false;
            };

            doingSearch = true;
            while (!open.isEmpty() && minedBlocks <= spell.config.VEIN_EXTENT.get()) {
                BlockPosSearch current = open.removeFirst();
                BlockState currBlock = level.getBlockState(current.loc);

                if (currBlock.is(block.getBlock())) {
                    // Post the block break event
                    BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(level, current.loc, currBlock, player);
                    MinecraftForge.EVENT_BUS.post(breakEvent);

                    // Handle if the event is canceled
                    if (!breakEvent.isCanceled()) {
                        level.destroyBlock(current.loc, true);
                        minedBlocks++;
                        current.notFoundDist = 0;
                    }
                }

                if (current.notFoundDist + 1 <= TOLERANCE.get())
                    for (Vec3 shift : shifts) {
                        BlockPos newPos = new BlockPos(new Vec3(current.loc.getX(), current.loc.getY(), current.loc.getZ()).add(shift));

                        if (checkVals.apply(newPos, current.notFoundDist + 1))
                            continue;


                        open.addLast(new BlockPosSearch(newPos, current.notFoundDist + 1));
                    }

                closed.addLast(current);
            }

            reduceMana(spell.config.MANA_COST.get(), player);

            doingSearch = false;
            event.setCanceled(true);
        }
    }

    @Override
    @SuppressWarnings("resource")
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (config.SPEED_LEVEL.get() > 0) {
            pTooltipComponents.add(new TextComponent("  - Gives ").withStyle(defaultColor)
                    .append(new TextComponent("Haste ").withStyle(Style.EMPTY.withColor(MobEffects.DIG_SPEED.getColor()))
                            .append(new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + (config.SPEED_LEVEL.get() - 1)))));
        }

        pTooltipComponents.add(enabledHeader);
        pTooltipComponents.add(new TextComponent("    - Allows vein mining of up to ").withStyle(defaultColor)
                .append(new TextComponent(MineBound.pluralize(config.VEIN_EXTENT.get(), "block")).withStyle(ChatFormatting.GOLD)));
        pTooltipComponents.add(new TextComponent("Additional copies increase the level of ").withStyle(defaultColor)
                .append(new TextComponent("Haste").withStyle(Style.EMPTY.withColor(MobEffects.DIG_SPEED.getColor())))
                .append(" effect"));
        pTooltipComponents.add(new TextComponent("Reduces ").withStyle(defaultColor)
                .append(new TextComponent("Manapool").withStyle(manaColorStyle))
                .append(" by ").append(new TextComponent(config.MANA_REDUCTION.get().toString()).withStyle(reductionColorStyle)));
        pTooltipComponents.add(new TextComponent("Vein mining costs ").withStyle(defaultColor)
                .append(new TextComponent(config.MANA_COST.get() + " Mana").withStyle(manaColorStyle)));

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Optional<UtilityToggle> toggle = player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).resolve();
            if (toggle.isPresent() && toggle.get().earth)
                pTooltipComponents.add(enabled);
            else
                pTooltipComponents.add(disabled);
        }
    }

    public static class EarthUtilitySpellConfig implements IConfig {

        public IntValue MANA_REDUCTION;
        public IntValue MANA_COST;
        public IntValue SPEED_LEVEL;
        public IntValue VEIN_EXTENT;

        public final ArmorTier LEVEL;
        private final int manaReduction;
        private final int manaCost;
        private final int speedLevel;
        private final int veinExtent;

        public EarthUtilitySpellConfig(int manaReduction, int manaCost, int miningSpeed, int veinExtent, ArmorTier level) {
            this.manaReduction = manaReduction;
            this.manaCost = manaCost;
            this.speedLevel = miningSpeed;
            this.veinExtent = veinExtent;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_REDUCTION = builder.comment("How much total mana will be reduced by").defineInRange("mana_reduction", manaReduction, 0, 10000);
            MANA_COST = builder.comment("How much mana will be spent when using vein mining").defineInRange("mana_cost", manaCost, 0, 10000);
            SPEED_LEVEL = builder.comment("Mining speed potion effect level").defineInRange("speed_level", speedLevel, 0, 10000);
            VEIN_EXTENT = builder.comment("How many blocks can be mined by the vein miner").defineInRange("vein_extent", veinExtent, 0, 64);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }

    protected static class BlockPosSearch {
        BlockPos loc;
        int notFoundDist;

        public BlockPosSearch(BlockPos loc, int notFoundDist) {
            this.loc = loc;
            this.notFoundDist = notFoundDist;
        }
    }
}
