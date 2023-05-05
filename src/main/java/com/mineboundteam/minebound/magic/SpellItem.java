package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.StreamSupport;

public abstract class SpellItem extends Item {
    public static final Style manaColorStyle = Style.EMPTY.withColor(MineBound.MANA_COLOR);
    public static final Style reductionColorStyle = Style.EMPTY.withColor(MineBound.REDUCTION_COLOR);
    public static final ChatFormatting defaultColor = ChatFormatting.GRAY;
    public static final ChatFormatting utilityColor = ChatFormatting.DARK_PURPLE;
    public static final ChatFormatting damageColor = ChatFormatting.RED;
    public static final ChatFormatting timeAndDistanceColor = ChatFormatting.DARK_GREEN;
    public static final ChatFormatting enchantmentColor = ChatFormatting.AQUA;
    public static final ChatFormatting itemAndEffectColor = ChatFormatting.WHITE;
    public final ArmorTier level;
    public final MagicType magicType;
    public final SpellType spellType;

    public SpellItem(Properties pProperties, ArmorTier level, MagicType magicType, SpellType spellType) {
        super(pProperties);
        this.level = level;
        this.magicType = magicType;
        this.spellType = spellType;
    }

    public static void reduceMana(int manaCost, Player p) {
        if (!p.level.isClientSide() && !p.isCreative())
            p.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                // Charge player mana
                int underflow = mana.subtractMana(manaCost);

                // Reduce player's armor charge directly
                if (underflow > 0) {
                    List<ItemStack> armors = StreamSupport.stream(p.getArmorSlots().spliterator(), false)
                            .filter(slot -> slot.getItem() instanceof MyrialArmorItem)
                            .toList();

                    if (armors.size() != 0) {
                        int amnt = underflow / armors.size();
                        underflow = 0;

                        for (ItemStack armorItem : armors) {
                            int damage = armorItem.getItem().getDamage(armorItem) + amnt;
                            if (damage > armorItem.getItem().getMaxDamage(armorItem)) {
                                underflow += damage - armorItem.getItem().getMaxDamage(armorItem);
                                damage = armorItem.getItem().getMaxDamage(armorItem);
                            }

                            armorItem.getItem().setDamage(armorItem, damage);
                        }
                    }
                }

                // Reduce player health.
                if (underflow > 0) {
                    p.hurt(DamageSource.STARVE, underflow / 3f);
                }
            });
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("Can be equipped in ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent("Tier ").withStyle(MyrialArmorItem.tierColors[level.getValue()])
                        .append(new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + level.getValue())))
                .append(" or higher armor"));
    }
}
