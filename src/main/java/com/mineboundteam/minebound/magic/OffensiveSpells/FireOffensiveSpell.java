package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.registry.ParticleRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class FireOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private static final HashMap<ArmorTier, RegistryObject<SimpleParticleType>> particles = new HashMap<>() {{
        put(ArmorTier.EFFIGY, ParticleRegistry.FIRE_OFFENSIVE_1);
        put(ArmorTier.SUIT, ParticleRegistry.FIRE_OFFENSIVE_2);
        put(ArmorTier.SYNERGY, ParticleRegistry.FIRE_OFFENSIVE_3);
    }};

    public FireOffensiveSpell(Properties properties, FireOffensiveSpellConfig config) {
        super(properties, config.LEVEL);

        this.manaCost = config.MANA_COST.get();
    }

    @Override
    public void use(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            CompoundTag isActive = new CompoundTag();
            isActive.putBoolean("minebound.fire_offensive.active", true);
            stack.setTag(isActive);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            CompoundTag isActive = new CompoundTag();
            isActive.putBoolean("minebound.fire_offensive.active", false);
            stack.setTag(isActive);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.side.isClient() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            boolean spellTriggered = triggerSpell(player, (ServerLevel) player.getLevel(), getSelectedSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL));
            if (!spellTriggered) {
                spellTriggered = triggerSpell(player, (ServerLevel) player.getLevel(), getSelectedSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL));
            }
            if (!spellTriggered) {
                spellTriggered = triggerSpell(player, (ServerLevel) player.getLevel(), player.getItemBySlot(EquipmentSlot.MAINHAND));
            }
            if (!spellTriggered) {
                triggerSpell(player, (ServerLevel) player.getLevel(), player.getItemBySlot(EquipmentSlot.OFFHAND));
            }
        }
    }

    protected static boolean triggerSpell(Player player, ServerLevel level, ItemStack selectedSpell) {
        if (selectedSpell.getItem() instanceof FireOffensiveSpell spell && selectedSpell.hasTag()) {
            boolean isActive = selectedSpell.getTag().getBoolean("minebound.fire_offensive.active");
            if (isActive) {
                float yRot = player.getYRot();
                float xRot = player.getXRot();
                double x = (0 - Math.sin(yRot * Math.PI / 180f));
                double y = (0 - Math.sin(xRot * Math.PI / 180f));
                double z = (Math.cos(yRot * Math.PI / 180f));
                for (int i = 0; i < 360; i++) {
//                    if (i % 10 == 0) {
//                    level.addParticle(particles.get(spell.level).get(),
//                            player.getX() + x - (z * Math.sin(i) * (Math.random() * 0.075d)),
//                            player.getY() + (player.isCrouching() ? 0.9d : 1.25d) + y + (Math.cos(i) * (Math.random() * 0.075d)),
//                            player.getZ() + z + (x * Math.sin(i) * (Math.random() * 0.075d)),
//                            x * 1000d - (Math.random() * (z * Math.sin(i) * 100d)),
//                            y * 1000d + (Math.random() * (Math.cos(i) * 100d)),
//                            z * 1000d + (Math.random() * (x * Math.sin(i) * 100d))
//                    );

                    level.sendParticles(particles.get(spell.level).get(),
                            player.getX() + x,
                            player.getY() + y + (player.isCrouching() ? 0.9d : 1.25d) + y,
                            player.getZ() + z,
                            1,
                            x / 10,
                            y / 10,
                            z / 10,
                            10d
                    );

//                    }
                }

//                if (player.level.getGameTime() % 20 == 0) {
//                    reduceMana(spell.manaCost, player);
//                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static class FireOffensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue FIREBALL_MANA_COST;
        public ForgeConfigSpec.IntValue FIRE_DISTANCE;
        public ArmorTier LEVEL;
        private final int manaCost;
        private final int fireBallManaCost;
        private final int fireDistance;

        public FireOffensiveSpellConfig(int manaCost, int fireballManaCost, int fireDistanceInBlocks, ArmorTier level) {
            this.manaCost = manaCost;
            this.fireBallManaCost = fireballManaCost;
            this.fireDistance = fireDistanceInBlocks;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            if (LEVEL == ArmorTier.SYNERGY) {
                FIREBALL_MANA_COST = builder.comment("Fireball mana cost").defineInRange("fireball_mana_cost", fireBallManaCost, 0, 10000);
            }
            FIRE_DISTANCE = builder.comment("Fire distance in blocks").defineInRange("fire_distance", fireDistance, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
