package com.mineboundteam.minebound.magic.DefensiveSpells;

import com.mineboundteam.minebound.block.registry.BlockRegistry;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightDefensiveSpell extends ActiveSpellItem {
    private final LightDefensiveSpellConfig config;

    public LightDefensiveSpell(Properties properties, LightDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.LIGHT, SpellType.DEFENSIVE);

        this.config = config;
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        if (!level.isClientSide()) {
            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            BlockItem magelight = (BlockItem) ForgeRegistries.ITEMS.getValue(BlockRegistry.MAGELIGHT.getId());
            UseOnContext context = new UseOnContext(level, player, InteractionHand.MAIN_HAND, magelight.getDefaultInstance(), hitResult);
            if (magelight.useOn(context) != InteractionResult.FAIL) {
                reduceMana(config.MANA_COST.get(), player);
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
        pTooltipComponents.add(new TextComponent("  - Places a ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("Magelight").withStyle(ChatFormatting.YELLOW))
                .append(" where the player is looking"));
        if (config.DESTROY_MAGELIGHT.get()) {
            pTooltipComponents.add(new TextComponent("  - ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(new TextComponent("Magelights").withStyle(ChatFormatting.YELLOW))
                    .append(" are destroyed after " + StringUtil.pluralize(config.MAGELIGHT_DURATION.get() / 60d, "minute")));
        }
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get(), " to place"));
    }

    public static class LightDefensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.BooleanValue DESTROY_MAGELIGHT;
        public ForgeConfigSpec.IntValue MAGELIGHT_DURATION;
        public final ArmorTier LEVEL;
        private final int manaCost;
        private final boolean timedDeletion;
        private final int magelightDuration;

        public LightDefensiveSpellConfig(int manaCost, boolean timedDeletion, int magelightDuration, ArmorTier level) {
            this.manaCost = manaCost;
            this.timedDeletion = timedDeletion;
            this.magelightDuration = magelightDuration;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Defensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            DESTROY_MAGELIGHT = builder.comment("Should the placed magelight be deleted after magelight_duration").define("destroy_magelight", timedDeletion);
            MAGELIGHT_DURATION = builder.comment("If destroy_magelight is true, after how long in seconds the magelight will be destroyed").defineInRange("magelight_duration", magelightDuration, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
