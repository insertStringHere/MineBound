package com.mineboundteam.minebound.magic.DefensiveSpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FireDefensiveSpell extends ActiveSpellItem {
    private final FireDefensiveSpellConfig config;

    public FireDefensiveSpell(Properties properties, FireDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.FIRE, SpellType.DEFENSIVE);

        this.config = config;
    }

    /**
     * Derived from {@link net.minecraft.world.item.FlintAndSteelItem#useOn(UseOnContext)}
     */
    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = hitResult.getBlockPos();
            BlockState blockstate = level.getBlockState(blockpos);
            if (!CampfireBlock.canLight(blockstate) && !CandleBlock.canLight(blockstate) && !CandleCakeBlock.canLight(blockstate)) {
                BlockPos relativeBlockPos = blockpos.relative(hitResult.getDirection());
                if (BaseFireBlock.canBePlacedAt(level, relativeBlockPos, player.getDirection())) {
                    level.playSound(player, relativeBlockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    BlockState relativeBlockState = BaseFireBlock.getState(level, relativeBlockPos);
                    level.setBlock(relativeBlockPos, relativeBlockState, 11);
                    level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, relativeBlockPos, stack);
                    }
                    reduceMana(config.MANA_COST.get(), player);
                }
            } else {
                level.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
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
        pTooltipComponents.add(new TextComponent("  - Functions identically to a ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("Flint and Steel").withStyle(ColorUtil.Tooltip.itemColor)));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get(), " per use"));
    }

    public static class FireDefensiveSpellConfig implements IConfig {
        public ForgeConfigSpec.IntValue MANA_COST;
        public final ArmorTier LEVEL;
        private final int manaCost;

        public FireDefensiveSpellConfig(int manaCost, ArmorTier level) {
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
