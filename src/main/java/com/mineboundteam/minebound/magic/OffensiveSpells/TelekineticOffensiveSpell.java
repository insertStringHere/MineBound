package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TelekineticOffensiveSpell extends ActiveSpellItem {

    private final int manaCostOnCast;
    private final int manaCostPerHit;

    public TelekineticOffensiveSpell(Properties properties, TelekineticOffensiveSpellConfig config) {
        super(properties, config.LEVEL);

        this.manaCostOnCast = config.MANA_COST_ON_CAST.get();
        this.manaCostPerHit = config.MANA_COST_PER_HIT.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            reduceMana(manaCostOnCast, player);
            if (player.getItemInHand(usedHand).isEmpty()) {
                player.setItemSlot(EquipmentSlot.MAINHAND, ItemRegistry.MYRIAL_MACHETE.get().getDefaultInstance());
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("When activated:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - If main hand is empty, places a ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Myrial Machete").withStyle(ChatFormatting.WHITE)).append(" into selected hotbar slot"));
        pTooltipComponents.add(new TextComponent("  - Unequipping the ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Myrial Machete").withStyle(ChatFormatting.WHITE)).append(" will cause it to vanish"));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(manaCostOnCast + " Mana").withStyle(Style.EMPTY.withColor(MineBound.MANA_COLOR)))
                                       .append(" to summon ")
                                       .append(new TextComponent("Myrial Machete").withStyle(ChatFormatting.WHITE))
                                       .append(", even if selected hotbar slot is not empty"));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(manaCostPerHit + " Mana").withStyle(Style.EMPTY.withColor(MineBound.MANA_COLOR)))
                                       .append(" per hit with the ")
                                       .append(new TextComponent("Myrial Machete").withStyle(ChatFormatting.WHITE)));
    }

    public static class TelekineticOffensiveSpellConfig implements IConfig {

        public final ArmorTier LEVEL;
        public ForgeConfigSpec.IntValue MANA_COST_ON_CAST;
        public ForgeConfigSpec.IntValue MANA_COST_PER_HIT;

        private final int manaCostOnCast;
        private final int manaCostPerHit;

        public TelekineticOffensiveSpellConfig(int manaCostOnCast, int manaCostPerHit, ArmorTier level) {
            this.manaCostOnCast = manaCostOnCast;
            this.manaCostPerHit = manaCostPerHit;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST_ON_CAST = builder.comment("Mana cost on spell cast").defineInRange("mana_cost_on_cast", manaCostOnCast, 0, 10000);
            MANA_COST_PER_HIT = builder.comment("Mana cost each time weapon deals damage").defineInRange("mana_cost_per_hit", manaCostPerHit, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
