package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;
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
    public static final String SHIELD_TAG = "minebound.shield_utility";
    public static final String COOLDOWN_TAG = "cooldown";
    public static final String HITS_TAG = "hits_remaining";

    public static final String MAX_TAG = "max_hits";

    public final ShieldUtilitySpellConfig config;

    public ShieldUtilitySpell(Properties properties, ShieldUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.SHIELD, SpellType.UTILITY);

        this.config = config;
    }
    

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void triggerSpell(LivingDamageEvent event) {
        if (event.getEntityLiving() instanceof Player player && !player.getLevel().isClientSide() && event.getSource() != DamageSource.STARVE && !event.getSource().isBypassInvul()) {
            ItemStack spellStack = getHighestEquippedSpellOfType(ShieldUtilitySpell.class, player);
            if (spellStack != null) {
                ShieldUtilitySpell spell = (ShieldUtilitySpell) spellStack.getItem();
                CompoundTag tag = spellStack.getOrCreateTagElement(SHIELD_TAG);
                float hitsRemaining = tag.getFloat(HITS_TAG);
                if (!tag.contains(HITS_TAG))
                    hitsRemaining = getShieldCount(player);

                if (hitsRemaining > 0) {
                    player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1f, 1f);
                    reduceMana(spell.config.MANA_COST.get(), player);

                    hitsRemaining -= event.getAmount();
                    if (hitsRemaining < 0) {
                        event.setAmount(-hitsRemaining);
                        hitsRemaining = 0;
                    } else {
                        event.setCanceled(true);
                    }

                    tag.putFloat(HITS_TAG, hitsRemaining);
                    tag.putInt(COOLDOWN_TAG, spell.config.RECOV_TICKS.get());
                    spellStack.getOrCreateTag().put(SHIELD_TAG, tag);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            ItemStack spellStack = getHighestEquippedSpellOfType(ShieldUtilitySpell.class, event.player);
            if (spellStack != null) {
                CompoundTag spellTag = spellStack.getOrCreateTagElement(SHIELD_TAG);
                int cooldown = spellTag.getInt(COOLDOWN_TAG);
                int shieldMax = getShieldCount(event.player);
                float hitsRemaining = spellTag.getFloat(HITS_TAG);

                if (cooldown > 0) {
                    spellTag.putInt(COOLDOWN_TAG, cooldown - 1);
                } else if (hitsRemaining < shieldMax) {
                    hitsRemaining += 0.5f;
                    spellTag.putInt(COOLDOWN_TAG, -1);
                }

                spellTag.putFloat(HITS_TAG, Math.min(hitsRemaining, shieldMax));
                spellTag.putInt(MAX_TAG, shieldMax);

                spellStack.getOrCreateTag().put(SHIELD_TAG, spellTag);
            }
        }
    }

    private static int getShieldCount(Player player) {
        int hitCount = 0;
        for (ShieldUtilitySpell spell : getEquippedSpellItemsOfType(ShieldUtilitySpell.class, player))
            hitCount += spell.config.TOTAL_HITS.get();
        return hitCount;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        // Add NBT data to item when it first enters the players inventory
        if (!pStack.hasTag() && !pLevel.isClientSide()) {
            CompoundTag spellTag = new CompoundTag();
            spellTag.putFloat(HITS_TAG, config.TOTAL_HITS.get());
            spellTag.putInt(COOLDOWN_TAG, -1);
            spellTag.putInt(MAX_TAG, -1);
            pStack.getOrCreateTag().put(SHIELD_TAG, spellTag);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("  - Adds ").withStyle(defaultColor)
                .append(new TextComponent(MineBound.pluralize(config.TOTAL_HITS.get(), "charge")).withStyle(ChatFormatting.AQUA))
                .append(" to a player's total, each absorbing half a heart of damage."));
        pTooltipComponents.add(new TextComponent("  - Charges will begin to replenish after ").withStyle(defaultColor)
                .append(new TextComponent("no charge").withStyle(ChatFormatting.AQUA))
                .append(" has been depleted for ")
                .append(new TextComponent((config.RECOV_TICKS.get() / 20) + " seconds at a rate of 10 charges per second").withStyle(timeAndDistanceColor)));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(defaultColor)
                .append(new TextComponent(config.MANA_COST.get() + " Mana").withStyle(manaColorStyle))
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
