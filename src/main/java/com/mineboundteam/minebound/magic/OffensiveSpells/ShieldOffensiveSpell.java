package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.registry.ClientRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShieldOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private final double damageReduction;
    private final double damageReflected;
    private boolean active = false;

    public ShieldOffensiveSpell(Properties properties, ShieldOffensiveSpellConfig config) {
        super(properties, config.LEVEL);

        this.manaCost = config.MANA_COST.get();
        this.damageReduction = config.DMG_REDUCTION.get();
        this.damageReflected = config.DMG_REFLECTED.get();

        MineBound.registerObject(this);
    }

    // TODO: tie in with keybindings based on where spell is equipped
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            active = ClientRegistry.PRIMARY_MAGIC.isDown();
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return null;
    }

    @SubscribeEvent
    public void triggerSpell(LivingAttackEvent event) {
        if (active && !event.getEntity().level.isClientSide() && event.getEntityLiving() instanceof Player player) {
            Entity sourceEntity = event.getSource().getEntity();
            if (sourceEntity != null) {
                float dmgAmount = event.getAmount();
                if ((1 - damageReduction) == 0) {
                    event.setCanceled(true);
                }
                sourceEntity.hurt(DamageSource.thorns(player), (float) (dmgAmount * damageReflected));
                super.reduceMana(manaCost, player);
            }
        }
    }

    @SubscribeEvent
    public void triggerSpell(LivingHurtEvent event) {
        if (active && !event.getEntity().level.isClientSide() && event.getEntityLiving() instanceof Player) {
            Entity sourceEntity = event.getSource().getEntity();
            if (sourceEntity != null) {
                float dmgAmount = event.getAmount();
                if ((1 - damageReduction) != 0) {
                    event.setAmount((float) (dmgAmount * (1 - damageReduction)));
                }
                // LivingAttackEvent will fall through to LivingHurtEvent if not canceled, thus no need to thorns and reduce mana here
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - Reduces damage taken from mobs by ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((int) (damageReduction * 100) + "%").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE)));
        pTooltipComponents.add(new TextComponent("  - Reflects ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((int) (damageReflected * 100) + "%").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE))
                                       .append(" of the initial damage").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       // TODO: Color subject to change once mana UI is implemented
                                       .append(new TextComponent(manaCost + " Mana").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE))
                                       .append(" per reflect").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("Spell is active while key bind is held").withStyle(ChatFormatting.GRAY));
    }

    public static class ShieldOffensiveSpellConfig implements IConfig {

        public final ArmorTier LEVEL;
        private final int manaCost;
        private final double damageReduction;
        private final double damageReflected;
        public IntValue MANA_COST;
        public DoubleValue DMG_REDUCTION;
        public DoubleValue DMG_REFLECTED;

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
