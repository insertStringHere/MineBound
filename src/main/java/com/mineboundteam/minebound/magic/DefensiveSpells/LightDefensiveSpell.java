package com.mineboundteam.minebound.magic.DefensiveSpells;

import com.mineboundteam.minebound.block.registry.BlockRegistry;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightDefensiveSpell extends ActiveSpellItem {
    private final int manaCost;

    public LightDefensiveSpell(Properties properties, LightDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.LIGHT, SpellType.DEFENSIVE);

        this.manaCost = config.MANA_COST.get();
    }

    @Override
    public void use(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockItem magelight = (BlockItem) ForgeRegistries.ITEMS.getValue(BlockRegistry.MAGELIGHT.getId());
                UseOnContext context = new UseOnContext(level, null, InteractionHand.MAIN_HAND, magelight.getDefaultInstance(), hitResult);
                if (magelight.useOn(context) != InteractionResult.FAIL) {
                    reduceMana(manaCost, player);
                }
            }
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, Level level, Player player) {
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, Player player) {
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("When activated:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - Places a ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Magelight").withStyle(ChatFormatting.YELLOW))
                                       .append(" where the player is looking"));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(manaCost + " Mana").withStyle(manaColorStyle))
                                       .append(" to place"));
    }

    public static class LightDefensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public final ArmorTier LEVEL;
        private final int manaCost;

        public LightDefensiveSpellConfig(int manaCost, ArmorTier level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Defensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
