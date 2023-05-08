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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
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
    public static final String ACTIVE_TAG = "minebound.shield_offensive.active";

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
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, true);
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, false);
    }

    @SubscribeEvent
    public static void triggerSpell(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayer player && event.getSource() != DamageSource.STARVE && !event.getSource().isBypassInvul()) {
            if (event.getSource().getEntity() instanceof LivingEntity sourceEntity) {
                boolean spellTriggered = triggerLivingAttackEvent(player, sourceEntity, getSelectedSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL), event);
                if (!spellTriggered) {
                    spellTriggered = triggerLivingAttackEvent(player, sourceEntity, getSelectedSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL), event);
                }
                if (!spellTriggered) {
                    triggerLivingAttackEvent(player, sourceEntity, player.getUseItem(), event);
                }
            }
        }
    }

    protected static boolean triggerLivingAttackEvent(Player player, LivingEntity sourceEntity, ItemStack selectedSpell, LivingAttackEvent event) {
        if (selectedSpell.getItem() instanceof ShieldOffensiveSpell spell) {
            if (selectedSpell.getOrCreateTag().getBoolean(ACTIVE_TAG)) {
                float dmgAmount = event.getAmount();
                if ((1 - spell.damageReduction) == 0) {
                    event.setCanceled(true);
                }
                sourceEntity.hurt(DamageSource.thorns(player), (float) (dmgAmount * spell.damageReflected));
                reduceMana(spell.manaCost, player);
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void triggerSpell(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayer player && event.getSource() != DamageSource.STARVE && !event.getSource().isBypassInvul()) {
            if (event.getSource().getEntity() instanceof LivingEntity) {
                boolean spellTriggered = triggerLivingHurtEvent(getSelectedSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL), event);
                if (!spellTriggered) {
                    spellTriggered = triggerLivingHurtEvent(getSelectedSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL), event);
                }
                if (!spellTriggered) {
                    triggerLivingHurtEvent(player.getUseItem(), event);
                }
            }
        }
    }

    protected static boolean triggerLivingHurtEvent(ItemStack selectedSpell, LivingHurtEvent event) {
        if (selectedSpell.getItem() instanceof ShieldOffensiveSpell spell) {
            if (selectedSpell.getOrCreateTag().getBoolean(ACTIVE_TAG)) {
                float dmgAmount = event.getAmount();
                if ((1 - spell.damageReduction) != 0) {
                    event.setAmount((float) (dmgAmount * (1 - spell.damageReduction)));
                }
                return true;
                // LivingAttackEvent will fall through to LivingHurtEvent if not canceled, thus no need to thorns and reduce mana here
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(new TextComponent("  - Reduces damage taken from mobs by ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.percentage(damageReduction)).withStyle(ChatFormatting.GOLD)));
        pTooltipComponents.add(new TextComponent("  - Reflects ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.percentage(damageReflected)).withStyle(ColorUtil.Tooltip.damageColor))
                .append(" of the initial damage"));
        pTooltipComponents.add(TooltipUtil.manaCost(manaCost, " per reflect"));
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