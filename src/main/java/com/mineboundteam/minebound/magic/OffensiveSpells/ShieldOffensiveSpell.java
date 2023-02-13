package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
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
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class ShieldOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private final ArmorTier level;
    private boolean active = false;
    private static final HashMap<ArmorTier, double[]> spellLevelDmgMult = new HashMap<>() {{
        // Reduce damage by 50% and reflect 40%
        put(ArmorTier.EFFIGY, new double[]{0.5, 0.4});
        // Reduce damage by 70% and reflect 60%
        put(ArmorTier.SUIT, new double[]{0.7, 0.6});
        // Reduce damage by 100% and reflect 80%
        put(ArmorTier.SYNERGY, new double[]{1.0, 0.8});
    }};

    public ShieldOffensiveSpell(Properties properties, ShieldOffensiveSpellConfig config) {
        super(properties, config.LEVEL);

        this.manaCost = config.MANA_COST.get();
        this.level = config.LEVEL;
    }

    // TODO: this code will execute in a different function once we work out how to execute spells from the keybindings
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            active = !active;
            player.displayClientMessage(new TextComponent("Active: " + active), false);
            // `triggerSpell` will only be called when spell is active
            // my theory is this is more performant than checking `active` in `triggerSpell`
            if (active) {
                MineBound.registerObject(this);
            } else {
                MineBound.unregisterObject(this);
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @SubscribeEvent
    public void triggerSpell(LivingAttackEvent event) {
        if (!event.getEntity().level.isClientSide() && event.getEntityLiving() instanceof Player player) {
            Entity sourceEntity = event.getSource().getEntity();
            if (sourceEntity != null) {
                float dmgAmount = event.getAmount();
                if ((1 - spellLevelDmgMult.get(level)[0]) == 0) {
                    event.setCanceled(true);
                }
                sourceEntity.hurt(DamageSource.thorns(player), (float) (dmgAmount * spellLevelDmgMult.get(level)[1]));
                super.reduceMana(manaCost, player);
            }
        }
    }

    @SubscribeEvent
    public void triggerSpell(LivingHurtEvent event) {
        if (!event.getEntity().level.isClientSide() && event.getEntityLiving() instanceof Player) {
            Entity sourceEntity = event.getSource().getEntity();
            if (sourceEntity != null) {
                float dmgAmount = event.getAmount();
                if ((1 - spellLevelDmgMult.get(level)[0]) != 0) {
                    event.setAmount((float) (dmgAmount * (1 - spellLevelDmgMult.get(level)[0])));
                }
                // LivingAttackEvent will fall through to LivingHurtEvent if not canceled, thus no need to thorns and reduce mana here
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - Reduces damage taken from mobs by ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((int) (spellLevelDmgMult.get(level)[0] * 100) + "%").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE)));
        pTooltipComponents.add(new TextComponent("  - Reflects ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((int) (spellLevelDmgMult.get(level)[1] * 100) + "%").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE))
                                       .append(" of the initial damage").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       // TODO: Color subject to change once mana UI is implemented
                                       .append(new TextComponent(manaCost + " Mana").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE))
                                       .append(" per reflect").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("Spell is active while key bind is held").withStyle(ChatFormatting.GRAY));
    }

    public static class ShieldOffensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public final ArmorTier LEVEL;

        private final int manaCost;

        public ShieldOffensiveSpellConfig(int manaCost, ArmorTier level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Shield Offensive");
            builder.push("Level " + LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop();
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
