package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ShieldOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private final double damageReduction;
    private final double damageReflected;

    public ShieldOffensiveSpell(Properties properties, ShieldOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.SHIELD, SpellType.OFFENSIVE);

        this.manaCost = config.MANA_COST.get();
        this.damageReduction = config.DMG_REDUCTION.get();
        this.damageReflected = config.DMG_REFLECTED.get();
    }


    @Override
    public void use(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            stack.getOrCreateTag().putBoolean("minebound.shield_offensive.active", true);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, Level level, Player player) {
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            stack.getOrCreateTag().putBoolean("minebound.shield_offensive.active", false);
        }
    }

    @SubscribeEvent
    public static void triggerSpell(LivingAttackEvent event) {
        if (!event.getEntity().level.isClientSide() && event.getEntityLiving() instanceof Player player) {
            Entity sourceEntity = event.getSource().getEntity();
            if (sourceEntity != null) {
                boolean spellTriggered = triggerLivingAttackEvent(player, sourceEntity, getSelectedSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL), event);
                if (!spellTriggered) {
                    spellTriggered = triggerLivingAttackEvent(player, sourceEntity, getSelectedSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL), event);
                }
                if (!spellTriggered) {
                    spellTriggered = triggerLivingAttackEvent(player, sourceEntity, player.getItemInHand(InteractionHand.MAIN_HAND), event);
                }
                if (!spellTriggered) {
                    triggerLivingAttackEvent(player, sourceEntity, player.getItemInHand(InteractionHand.OFF_HAND), event);
                }
            }
        }
    }

    protected static boolean triggerLivingAttackEvent(Player player, Entity sourceEntity, ItemStack selectedSpell, LivingAttackEvent event) {
        boolean spellTriggered = false;
        if (selectedSpell.getItem() instanceof ShieldOffensiveSpell spell && selectedSpell.hasTag()) {
            boolean isActive = selectedSpell.getOrCreateTag().getBoolean("minebound.shield_offensive.active");
            if (isActive) {
                float dmgAmount = event.getAmount();
                if ((1 - spell.damageReduction) == 0) {
                    event.setCanceled(true);
                }
                sourceEntity.hurt(DamageSource.thorns(player), (float) (dmgAmount * spell.damageReflected));
                reduceMana(spell.manaCost, player);
                spellTriggered = true;
            }
        }
        return spellTriggered;
    }

    @SubscribeEvent
    public static void triggerSpell(LivingHurtEvent event) {
        if (!event.getEntity().level.isClientSide() && event.getEntityLiving() instanceof Player player) {
            Entity sourceEntity = event.getSource().getEntity();
            if (sourceEntity != null) {
                boolean spellTriggered = triggerLivingHurtEvent(getSelectedSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL), event);
                if (!spellTriggered) {
                    spellTriggered = triggerLivingHurtEvent(getSelectedSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL), event);
                }
                if (!spellTriggered) {
                    spellTriggered = triggerLivingHurtEvent(player.getItemInHand(InteractionHand.MAIN_HAND), event);
                }
                if (!spellTriggered) {
                    triggerLivingHurtEvent(player.getItemInHand(InteractionHand.OFF_HAND), event);
                }
            }
        }
    }

    protected static boolean triggerLivingHurtEvent(ItemStack selectedSpell, LivingHurtEvent event) {
        boolean spellTriggered = false;
        if (selectedSpell.getItem() instanceof ShieldOffensiveSpell spell && selectedSpell.hasTag()) {
            boolean isActive = selectedSpell.getOrCreateTag().getBoolean("minebound.shield_offensive.active");
            if (isActive) {
                float dmgAmount = event.getAmount();
                if ((1 - spell.damageReduction) != 0) {
                    event.setAmount((float) (dmgAmount * (1 - spell.damageReduction)));
                }
                spellTriggered = true;
                // LivingAttackEvent will fall through to LivingHurtEvent if not canceled, thus no need to thorns and reduce mana here
            }
        }
        return spellTriggered;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - Reduces damage taken from mobs by ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((int) (damageReduction * 100) + "%").withStyle(ChatFormatting.GOLD)));
        pTooltipComponents.add(new TextComponent("  - Reflects ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((int) (damageReflected * 100) + "%").withStyle(ChatFormatting.RED))
                                       .append(" of the initial damage").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(manaCost + " Mana").withStyle(manaColorStyle))
                                       .append(" per reflect").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("Spell is active while key bind is held").withStyle(ChatFormatting.GRAY));
    }

    public static class ShieldOffensiveSpellConfig implements IConfig {

        public IntValue MANA_COST;
        public DoubleValue DMG_REDUCTION;
        public DoubleValue DMG_REFLECTED;
        public final ArmorTier LEVEL;
        private final int manaCost;
        private final double damageReduction;
        private final double damageReflected;

        public ShieldOffensiveSpellConfig(int manaCost, double damageReduction, double damageReflected, ArmorTier level) {
            this.manaCost = manaCost;
            this.damageReduction = damageReduction;
            this.damageReflected = damageReflected;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            DMG_REDUCTION = builder.comment("Damage Reduction").defineInRange("dmg_reduction", damageReduction, 0.0, 1.0);
            DMG_REFLECTED = builder.comment("Damage Reflected").defineInRange("dmg_reflected", damageReflected, 0.0, 10.0);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
