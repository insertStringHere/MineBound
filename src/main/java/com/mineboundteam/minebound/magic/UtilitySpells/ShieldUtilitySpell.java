package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ShieldUtilitySpell extends PassiveSpellItem {

    private final int manaCost;
    private final int totalHits;
    private final int recovCooldown;

    public ShieldUtilitySpell(Properties properties, ShieldUtilitySpellConfig config) {
        super(properties, config.LEVEL);

        this.manaCost = config.MANA_COST.get();
        this.totalHits = config.TOTAL_HITS.get();
        this.recovCooldown = config.RECOV_TICKS.get();
    }

    @SubscribeEvent
    public static void triggerSpell(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            ItemStack spellStack = getHighestEquippedSpellOfType(ShieldUtilitySpell.class, player);
            if (spellStack != null) {
                ShieldUtilitySpell spell = (ShieldUtilitySpell) spellStack.getItem();
                int hitsRemaining = spell.totalHits;
                if (spellStack.hasTag()) {
                    hitsRemaining = spellStack.getTag().getInt("minebound.shield_utility.hits_remaining");
                }
                if (hitsRemaining > 0) {
//                    player.playSound(SoundEvents.SHIELD_BLOCK, 1f, 1f);
                    if (!player.getLevel().isClientSide() && event.getSource() instanceof EntityDamageSource) {
                        System.out.println(hitsRemaining);
                        // TODO: canceling the event, and therefore damage, prevents it from firing client side so no sound can be played client side
                        event.setCanceled(true);
                        spell.reduceMana(spell.manaCost, player);

                        CompoundTag tag = new CompoundTag();
                        tag.putInt("minebound.shield_utility.hits_remaining", hitsRemaining - 1);
                        tag.putInt("minebound.shield_utility.cooldown", spell.recovCooldown);
                        spellStack.setTag(tag);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            ItemStack spellStack = getHighestEquippedSpellOfType(ShieldUtilitySpell.class, player);
            if (spellStack != null) {
                ShieldUtilitySpell spell = (ShieldUtilitySpell) spellStack.getItem();
                if (spellStack.hasTag()) {
                    int cooldown = spellStack.getTag().getInt("minebound.shield_utility.cooldown");
                    CompoundTag tag = new CompoundTag();
                    if (cooldown > 0) {
                        tag.putInt("minebound.shield_utility.hits_remaining", spellStack.getTag().getInt("minebound.shield_utility.hits_remaining"));
                        tag.putInt("minebound.shield_utility.cooldown", cooldown - 1);
                        spellStack.setTag(tag);
                    } else if (cooldown == 0) {
                        tag.putInt("minebound.shield_utility.hits_remaining", spell.totalHits);
                        tag.putInt("minebound.shield_utility.cooldown", -1);
                        spellStack.setTag(tag);
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        // Add NBT data to item when it first enters the players inventory
        if (!pStack.hasTag() && !pLevel.isClientSide()) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("minebound.shield_utility.hits_remaining", totalHits);
            tag.putInt("minebound.shield_utility.cooldown", -1);
            pStack.setTag(tag);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("  - Stores ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(totalHits + " charges").withStyle(ChatFormatting.AQUA))
                                       .append(", each fully negating the damage from an attack"));
        pTooltipComponents.add(new TextComponent("  - Charges will be fully replenished after ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("no charge").withStyle(ChatFormatting.AQUA))
                                       .append(" has been depleted for ")
                                       .append(new TextComponent((recovCooldown / 20) + " seconds").withStyle(ChatFormatting.DARK_GREEN)));
        pTooltipComponents.add(new TextComponent("Depletes ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("1 charge").withStyle(ChatFormatting.AQUA))
                                       .append(" and costs ")
                                       .append(new TextComponent(manaCost + " Mana").withStyle(ChatFormatting.BLUE))
                                       .append(" per attack received"));
    }

    public static class ShieldUtilitySpellConfig implements IConfig {

        public final ArmorTier LEVEL;
        private final int manaCost;
        private final int totalHits;
        private final int recovCooldownTicks;
        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue TOTAL_HITS;
        public ForgeConfigSpec.IntValue RECOV_TICKS;

        public ShieldUtilitySpellConfig(int manaCost, int totalHits, int recovCooldownTicks, ArmorTier level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
            this.totalHits = totalHits;
            this.recovCooldownTicks = recovCooldownTicks;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            TOTAL_HITS = builder.comment("Hits before spell goes on cool down").defineInRange("total_hits", totalHits, 0, 10000);
            RECOV_TICKS = builder.comment("Time in ticks before the shield fully recovers (20 ticks = 1 second)").defineInRange("recov_tick", recovCooldownTicks, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
