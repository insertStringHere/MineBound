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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    protected static UUID autoStepID = new UUID(1837183113, 22255113);

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            List<ElectricUtilitySpell> equippedSpells = getEquippedSpellItemsOfType(ElectricUtilitySpell.class, player);
            if (equippedSpells.size() > 0) {
                ElectricUtilitySpell spell = getHighestSpellItem(equippedSpells);

                if(spell.level.getValue() > ArmorTier.SUIT.getValue()){
                    AttributeInstance autoStep = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());

                    if (autoStep.getModifier(autoStepID) == null) 
                        autoStep.addTransientModifier(new AttributeModifier(autoStepID, "AutoStep", 1, Operation.ADDITION));
                }

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

                AttributeInstance autoStep = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());

                if (autoStep.getModifier(autoStepID) != null) 
                    autoStep.removeModifier(autoStepID);
            }
        }
    }

    // Want this to run first so all further calculations use this value
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("resource")
    // Taken from AbstractClientPlayer
    // Instead of carefully undoing the calculations, if the player has speed boost from electric spell,
    // disable speed FOV altogether
    public static void removeFOV(FOVModifierEvent event){
        Player player = event.getEntity();
        if(getHighestSpellItem(ElectricUtilitySpell.class, player) != null){
            float f = 1.0F;
        
            if (player.getAbilities().flying) {
                f *= 1.1F;
            }

            ItemStack itemstack = player.getUseItem();
            if (player.isUsingItem()) {
                if (itemstack.is(Items.BOW)) {
                    int i = player.getTicksUsingItem();
                    float f1 = (float)i / 20.0F;
                    if (f1 > 1.0F) {
                    f1 = 1.0F;
                    } else {
                    f1 *= f1;
                    }

                    f *= 1.0F - f1 * 0.15F;
                } else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.isScoping()) {
                    f = 0.1f;
                }
            }

            event.setNewfov(f);
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
        if(level.getValue() > ArmorTier.SUIT.getValue()){
            pTooltipComponents.add(new TextComponent("  - Allows the player to automatically step up ").withStyle(ChatFormatting.GRAY)
                                        .append(new TextComponent("one block.").withStyle(ChatFormatting.AQUA)));
        }
        pTooltipComponents.add(new TextComponent("Additional copies increase the ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Speed").withStyle(ChatFormatting.WHITE))
                                       .append(" effect"));
        pTooltipComponents.add(new TextComponent("Reduces ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Manapool").withStyle(manaColorStyle))
                                       .append(" by ").append(new TextComponent(totalManaReduction + "").withStyle(reductionColorStyle)));
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
