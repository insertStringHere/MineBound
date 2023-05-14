package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.tool.MyrialSwordItem;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TelekineticOffensiveSpell extends ActiveSpellItem {
    private final TelekineticOffensiveSpellConfig config;

    public TelekineticOffensiveSpell(Properties properties, TelekineticOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.TELEKINETIC, SpellType.OFFENSIVE);

        this.config = config;
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        if (!level.isClientSide() && !(config.SWORD_ITEM.get().getClass().isInstance(player.getItemBySlot(EquipmentSlot.MAINHAND).getItem()))) {
            reduceMana(config.MANA_COST_ON_CAST.get(), player);
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                player.setItemSlot(EquipmentSlot.MAINHAND, config.SWORD_ITEM.get().getDefaultInstance());
            }
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("When activated:").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(new TextComponent("  - If main hand is empty, places a ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(TooltipUtil.itemName(config.SWORD_ITEM.get()))
                .append(" into selected hotbar slot"));
        pTooltipComponents.add(new TextComponent("  - Unequipping the ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(TooltipUtil.itemName(config.SWORD_ITEM.get()))
                .append(" will cause it to vanish"));
        if (config.hasProjectile) {
            pTooltipComponents.add(new TextComponent("  - Right clicking with the ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(TooltipUtil.itemName(config.SWORD_ITEM.get()))
                    .append(" will throw it"));
            pTooltipComponents.add(new TextComponent("    - Will hover ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(new TextComponent(StringUtil.pluralize(config.PROJECTILE_RANGE.get(), "block")).withStyle(ColorUtil.Tooltip.timeAndDistanceColor))
                    .append(new TextComponent(" from the player and move where they look")));
            pTooltipComponents.add(new TextComponent("    - Will deal ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(new TextComponent(StringUtil.pluralize(config.PROJECTILE_DMG.get() / 2d, "heart") + " of damage").withStyle(ColorUtil.Tooltip.damageColor))
                    .append(" to mobs it hits"));
            pTooltipComponents.add(new TextComponent("    - Right clicking again will return it to the player").withStyle(ColorUtil.Tooltip.defaultColor));
        }
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST_ON_CAST.get(), " to summon ")
                .append(TooltipUtil.itemName(config.SWORD_ITEM.get()))
                .append(", even if main hand is not empty"));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST_PER_HIT.get(), " per hit with the ")
                .append(TooltipUtil.itemName(config.SWORD_ITEM.get())));
    }

    public static class TelekineticOffensiveSpellConfig implements IConfig {

        public final ArmorTier LEVEL;
        public ForgeConfigSpec.IntValue MANA_COST_ON_CAST;
        public ForgeConfigSpec.IntValue MANA_COST_PER_HIT;
        public ForgeConfigSpec.DoubleValue PROJECTILE_DMG;
        public ForgeConfigSpec.DoubleValue PROJECTILE_RANGE;
        public final RegistryObject<MyrialSwordItem> SWORD_ITEM;
        public final RegistryObject<Item> PLACEHOLDER_ITEM;
        private final int manaCostOnCast;
        private final int manaCostPerHit;
        public final boolean hasProjectile;
        private final double projectileDamage;
        private final double projectileRange;


        public TelekineticOffensiveSpellConfig(int manaCostOnCast, int manaCostPerHit, RegistryObject<MyrialSwordItem> swordItem,
                                               RegistryObject<Item> placeholderItem, boolean hasProjectile, double projectileDamage,
                                               double projectileRange, ArmorTier level) {
            this.manaCostOnCast = manaCostOnCast;
            this.manaCostPerHit = manaCostPerHit;
            this.SWORD_ITEM = swordItem;
            this.PLACEHOLDER_ITEM = placeholderItem;
            this.hasProjectile = hasProjectile;
            this.projectileDamage = projectileDamage;
            this.projectileRange = projectileRange;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST_ON_CAST = builder.comment("Mana cost on spell cast").defineInRange("mana_cost_on_cast", manaCostOnCast, 0, 10000);
            MANA_COST_PER_HIT = builder.comment("Mana cost each time weapon deals damage").defineInRange("mana_cost_per_hit", manaCostPerHit, 0, 10000);
            if (hasProjectile) {
                PROJECTILE_DMG = builder.comment("Damage dealt by the projectile sword (2 damage = 1 heart)").defineInRange("projectile_dmg", projectileDamage, 0, 10000);
                PROJECTILE_RANGE = builder.comment("Maximum range in blocks of the projectile sword").defineInRange("projectile_range", projectileRange, 0, 10000);
            }
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}