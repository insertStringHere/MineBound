package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void triggerSpell(LivingDamageEvent event) {
        if (event.getEntityLiving() instanceof Player player && !player.getLevel().isClientSide() && event.getSource() != DamageSource.STARVE && !event.getSource().isBypassInvul()) {
            ItemStack spellStack = getHighestEquippedSpellOfType(ShieldUtilitySpell.class, player);
            if (spellStack != null) {
                ShieldUtilitySpell spell = (ShieldUtilitySpell) spellStack.getItem();
                CompoundTag tag = spellStack.getOrCreateTagElement("minebound.shield_utility");
                float hitsRemaining = tag.getFloat("hits_remaining");
                if (!tag.contains("hits_remaining")) 
                    hitsRemaining = getShieldCount(player);
        
                if (hitsRemaining > 0) {
                    player.getLevel().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1f, 1f);
                    reduceMana(spell.manaCost, player);
                    
                    hitsRemaining-=event.getAmount();
                    if(hitsRemaining < 0){
                        event.setAmount(-hitsRemaining);
                        hitsRemaining = 0;
                    } else {
                        event.setCanceled(true);
                    }
                    
                    tag.putFloat("hits_remaining", hitsRemaining - event.getAmount());
                    tag.putInt("cooldown", spell.recovCooldown);
                    spellStack.getTag().put("minebound.shield_utility", tag);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            ItemStack spellStack = getHighestEquippedSpellOfType(ShieldUtilitySpell.class, event.player);
            if (spellStack != null) {
                CompoundTag spellTag = spellStack.getOrCreateTagElement("minebound.shield_utility");
                int cooldown = spellTag.getInt("cooldown");
                int shieldMax = getShieldCount(event.player);
                float hitsRemaining = spellTag.getFloat("hits_remaining");

                if (cooldown > 0) {
                    spellTag.putInt("cooldown", cooldown - 1);
                } else if (hitsRemaining < shieldMax) {
                    spellTag.putFloat("hits_remaining", Math.min(hitsRemaining + 0.5f, shieldMax));
                    spellTag.putInt("cooldown", -1);
                }

                spellStack.getOrCreateTag().put("minebound.shield_utility", spellTag);
            }
        }
    }

    private static int getShieldCount(Player player){
        int hitCount = 0;
        for(ShieldUtilitySpell spell : getEquippedSpellItemsOfType(ShieldUtilitySpell.class, player))
            hitCount += spell.totalHits;
        return hitCount;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        // Add NBT data to item when it first enters the players inventory
        if (!pStack.hasTag() && !pLevel.isClientSide()) {
            CompoundTag spellTag = new CompoundTag();
            spellTag.putFloat("hits_remaining", totalHits);
            spellTag.putInt("cooldown", -1);
            pStack.getOrCreateTag().put("minebound.shield_utility", spellTag);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While equipped in a ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("utility slot").withStyle(ChatFormatting.DARK_PURPLE))
                                       .append(":"));
        pTooltipComponents.add(new TextComponent("  - Adds ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(totalHits + " charges").withStyle(ChatFormatting.AQUA))
                                       .append(" to a player's total, each absorbing half a heart of damage."));
        pTooltipComponents.add(new TextComponent("  - Charges will begin to replenish after ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("no charge").withStyle(ChatFormatting.AQUA))
                                       .append(" has been depleted for ")
                                       .append(new TextComponent((recovCooldown / 20) + " seconds").withStyle(ChatFormatting.DARK_GREEN))
                                       .append(new TextComponent(" at a rate of ").withStyle(ChatFormatting.DARK_GREEN))
                                       .append(new TextComponent("10 charges per second")).withStyle(ChatFormatting.DARK_GREEN));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(manaCost + " Mana").withStyle(manaColorStyle))
                                       .append(" every time damage is absorbed"));
    }

    public static class ShieldUtilitySpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue TOTAL_HITS;
        public ForgeConfigSpec.IntValue RECOV_TICKS;
        public final ArmorTier LEVEL;
        private final int manaCost;
        private final int totalHits;
        private final int recovCooldownTicks;

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
            RECOV_TICKS = builder.comment("Time in ticks before the shield fully recovers (20 ticks = 1 second)").defineInRange("recovery_ticks", recovCooldownTicks, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
