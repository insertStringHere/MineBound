package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ElectricUtilitySpell extends PassiveSpellItem {

    private final int totalManaReduction;
    private final int speedEffectLevel;
    private final boolean thorns;

    public ElectricUtilitySpell(Properties properties, ElectricUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.ELECTRIC, SpellType.UTILITY);

        this.totalManaReduction = config.MANA_REDUCTION.get();
        this.speedEffectLevel = config.SPEED_LEVEL.get();
        this.thorns = config.THORNS.get();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            List<ElectricUtilitySpell> equippedSpells = getEquippedSpellItemsOfType(ElectricUtilitySpell.class, player);
            if (equippedSpells.size() > 0) {
                ElectricUtilitySpell spell = getHighestSpellItem(equippedSpells);

                int manaReduction = 0;
                // Mob effect levels start at 0, so this starts at -1 to compensate for the off by 1
                int speedLevel = -1;
                for (ElectricUtilitySpell s : equippedSpells) {
                    manaReduction += s.totalManaReduction;
                    speedLevel += s.speedEffectLevel;
                }

                // Java doesn't like non "final" variables being used in lambdas
                int finalManaReduction = manaReduction;
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("electric_utility", -finalManaReduction));

                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, speedLevel, false, false));

                if (spell.thorns) {
                    for (EquipmentSlot e : EquipmentSlot.values()) {
                        if (e.getType() == EquipmentSlot.Type.ARMOR && player.getItemBySlot(e).getItem() instanceof MyrialArmorItem) {
                            // Apply corresponding thorns enchantment if not already applied or a lower level is currently applied by lower tier version of spell
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.THORNS, player.getItemBySlot(e)) < spell.level.getValue()) {
                                EnchantmentHelper.setEnchantments(Collections.singletonMap(Enchantments.THORNS, spell.level.getValue()), player.getItemBySlot(e));
                            }
                        }
                    }
                }
            } else {
                // Remove total mana reduction
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("electric_utility", 0));

                // Remove any thorns enchantments previously applied
                for (EquipmentSlot e : EquipmentSlot.values()) {
                    if (e.getType() == EquipmentSlot.Type.ARMOR && player.getItemBySlot(e).getItem() instanceof MyrialArmorItem) {
                        for (ArmorTier tier : ArmorTier.values()) {
                            player.getItemBySlot(e).getEnchantmentTags().remove(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(Enchantments.THORNS), (byte) tier.getValue()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While equipped in a ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("utility slot").withStyle(ChatFormatting.DARK_PURPLE))
                                       .append(":"));
        pTooltipComponents.add(new TextComponent("  - Gives ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Speed ")
                                                       .append(new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + (speedEffectLevel - 1))).withStyle(ChatFormatting.WHITE)));
        if (thorns) {
            pTooltipComponents.add(new TextComponent("  - Enchants equipped Myrial Armor with ").withStyle(ChatFormatting.GRAY)
                                           .append(new TextComponent("Thorns ")
                                                           .append(new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + (level.getValue() - 1))).withStyle(ChatFormatting.AQUA)));
        }
        pTooltipComponents.add(new TextComponent("Additional copies increase the ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Speed").withStyle(ChatFormatting.WHITE))
                                       .append(" effect"));
        pTooltipComponents.add(new TextComponent("Reduces ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Manapool").withStyle(manaColorStyle))
                                       .append(" by ").append(new TextComponent(totalManaReduction + "").withStyle(manaColorStyle)));
    }

    public static class ElectricUtilitySpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_REDUCTION;
        public ForgeConfigSpec.IntValue SPEED_LEVEL;
        public ForgeConfigSpec.BooleanValue THORNS;
        public final ArmorTier LEVEL;
        private final int manaReduction;
        private final int speedLevel;
        private final boolean thorns;

        public ElectricUtilitySpellConfig(int manaReduction, int speedLevel, boolean thorns, ArmorTier level) {
            this.manaReduction = manaReduction;
            this.speedLevel = speedLevel;
            this.thorns = thorns;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_REDUCTION = builder.comment("How much total mana will be reduced by").defineInRange("mana_reduction", manaReduction, 0, 10000);
            SPEED_LEVEL = builder.comment("Speed potion effect level").defineInRange("speed_level", speedLevel, 0, 10000);
            THORNS = builder.comment("Gives the thorns enchantment to equipped Myrial Armor").define("thorns", thorns);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
