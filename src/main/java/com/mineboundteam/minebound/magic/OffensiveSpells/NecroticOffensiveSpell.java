package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.LinkedList;
import java.util.List;


@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class NecroticOffensiveSpell extends ActiveSpellItem {
    public static DoubleValue FOOD_REDUCTION;

    private final NecroticOffensiveSpellConfig config;

    public final static String ACTIVE_TAG = MineBound.MOD_ID + ".necrotic_offensive.active";
    public final static String TICK_TAG = MineBound.MOD_ID + ".necrotic_offensive.ticks";

    public NecroticOffensiveSpell(Properties properties, NecroticOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.NECROTIC, SpellType.OFFENSIVE);

        this.config = config;
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, true);
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
        if (stack.getOrCreateTag().getBoolean(ACTIVE_TAG)) {
            int ticks = stack.getOrCreateTag().getInt(TICK_TAG);
            if (ticks < 20)
                ticks++;
            else {
                reduceMana(config.MANA_COST.get(), player);
                ticks = 0;
            }
            stack.getOrCreateTag().putInt(TICK_TAG, ticks);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, false);
        stack.getOrCreateTag().putInt(TICK_TAG, 0);
    }

    @SubscribeEvent
    public static void onAttack(LivingDamageEvent event) {
        if (event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity() instanceof ServerPlayer player) {
            LinkedList<ItemStack> spellStack = new LinkedList<>();
            spellStack.addLast(player.getUseItem());
            spellStack.addLast(getSelectedSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL));
            spellStack.addLast(getSelectedSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL));

            for (ItemStack spell : spellStack) {
                if (spell.getOrCreateTag().getBoolean(ACTIVE_TAG) && spell.getItem() instanceof NecroticOffensiveSpell spellItem) {
                    float amount = event.getAmount();
                    amount += amount * spellItem.config.DMG_BOOST.get();
                    event.setAmount(amount);

                    if (player.getHealth() < player.getMaxHealth())
                        player.heal((float) (amount * spellItem.config.HEALTH_AMT.get()));
                    else {
                        FoodData food = player.getFoodData();
                        int recoveryAmt = (int) (amount * spellItem.config.HEALTH_AMT.get() * NecroticOffensiveSpell.FOOD_REDUCTION.get());
                        recoveryAmt = recoveryAmt == 0 ? 1 : recoveryAmt;

                        if (food.getFoodLevel() < 20)
                            food.setFoodLevel(food.getFoodLevel() + recoveryAmt);
                        else
                            food.setSaturation(food.getSaturationLevel() + recoveryAmt);
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(new TextComponent("  - Increases damage dealt by ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.percentage(config.DMG_BOOST.get())).withStyle(ColorUtil.Tooltip.damageColor)));
        pTooltipComponents.add(new TextComponent("  - Restores ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.percentage(config.HEALTH_AMT.get())).withStyle(ChatFormatting.GOLD))
                .append(" of damage dealt as health"));
        pTooltipComponents.add(new TextComponent("  - If health is full, restores ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.percentage(config.HEALTH_AMT.get() * FOOD_REDUCTION.get())).withStyle(ChatFormatting.GOLD))
                .append(" of damage dealt as hunger"));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get(), " per second of use"));
    }

    public static class NecroticOffensiveSpellConfig implements IConfig {

        public IntValue MANA_COST;
        public DoubleValue DMG_BOOST;
        public DoubleValue HEALTH_AMT;
        public final ArmorTier LEVEL;
        private final int manaCost;
        private final double damageBoost;
        private final double percentHealed;


        public NecroticOffensiveSpellConfig(int manaCost, double damageBoost, double percentHealed, ArmorTier level) {
            this.manaCost = manaCost;
            this.damageBoost = damageBoost;
            this.percentHealed = percentHealed;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            DMG_BOOST = builder.comment("Damage Boost").defineInRange("dmg_boost", damageBoost, 0.0, 10.0);
            HEALTH_AMT = builder.comment("Damage Heal Percent").defineInRange("health_amt", percentHealed, 0.0, 10.0);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }

}
