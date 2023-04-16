package com.mineboundteam.minebound.magic.DefensiveSpells;

import java.util.HashMap;
import java.util.List;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;

import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class EarthDefensiveSpell extends ActiveSpellItem {
    public static BooleanValue vanillaBreak;
    protected final EarthDefensiveSpellConfig config; 

    public EarthDefensiveSpell(Properties properties, EarthDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.EARTH, SpellType.DEFENSIVE);
        this.config = config;        
    }

    protected final HashMap<Player, Tuple<BlockPos, Float>> breakProgress = new HashMap<Player, Tuple<BlockPos, Float>>(20);

    @Override
    public void use(ItemStack stack, Level level, Player player) {      
        var hitResult = player.pick((float) player.getAttackRange() + 1.5f, 1F, false);

        if (hitResult != null && hitResult.getType() == Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult) hitResult;
            BlockPos blockpos = blockhitresult.getBlockPos();
            if (!level.isEmptyBlock(blockpos)) {
                BlockState block = level.getBlockState(blockpos);
                if(ForgeEventFactory.doPlayerHarvestCheck(player, block, !block.requiresCorrectToolForDrops() || TierSortingRegistry.isCorrectTierForDrops(config.MINING_LEVEL.get(), block))){

                    if (vanillaBreak.get() && !player.isCreative()) {
                        synchronized(breakProgress){
                            Tuple<BlockPos, Float> data = breakProgress.getOrDefault(player, new Tuple<BlockPos,Float>(blockpos, 0f));

                            if(!data.getA().equals(blockpos)){
                                data = new Tuple<BlockPos,Float>(blockpos, 0f);
                            }

                            data.setB(data.getB() + getDestroySpeed(player, level, block, blockpos));
                            level.destroyBlockProgress(player.getId(), blockpos, (int)(data.getB() * 10));
                            breakProgress.put(player, data); 

                            if(data.getB() >= 1f && !level.isClientSide()){
                                level.destroyBlock(blockpos, true);
                                breakProgress.remove(player);
                                reduceMana(config.MANA_COST_ON_CAST.get(), player);
                            }
                        }
                    } else if (!level.isClientSide()){
                        level.destroyBlock(blockpos, true);
                        reduceMana(config.MANA_COST_ON_CAST.get(), player);
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, Player player) {
        if(breakProgress.containsKey(player)) {
            level.destroyBlockProgress(player.getId(), breakProgress.get(player).getA(), -1);
        }
        breakProgress.remove(player);
    }

    protected float getDestroySpeed(Player player, Level level, BlockState blockState, BlockPos pos){
        float destroySpeed = blockState.getDestroySpeed(level, pos);
        if (destroySpeed == -1.0F) {
            return 0.0F;
        }

        float f = config.SPEED_MODIFIER.get().floatValue(); 
        if (MobEffectUtil.hasDigSpeed(player)) {
            f *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
            }
    
            if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f1;
            switch(player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                f1 = 8.1E-4F;
            }
    
            f *= f1;
        }

        if (player.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player)) {
        f /= 5.0F;
        }

        f = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed(player, blockState, f, pos);
        return f / destroySpeed / 90f;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("Mines blocks using the mining level of ").withStyle(ChatFormatting.GRAY)
                                        .append(new TextComponent(config.MINING_LEVEL.get().name()).withStyle(ChatFormatting.GOLD)));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(config.MANA_COST_ON_CAST.get() + " Mana").withStyle(manaColorStyle))
                                       .append(" per block broken").withStyle(ChatFormatting.GRAY));
    }

    public static class EarthDefensiveSpellConfig implements IConfig {

        public final ArmorTier LEVEL;
        public IntValue MANA_COST_ON_CAST;
        public EnumValue<Tiers> MINING_LEVEL; 
        public DoubleValue SPEED_MODIFIER;
        private final int manaCostOnCast;
        private final Tiers miningLevel; 
        private final float speedModifier;

        public EarthDefensiveSpellConfig(int manaCostOnCast, Tiers miningLevel, float miningSpeed, ArmorTier level) {
            this.manaCostOnCast = manaCostOnCast;
            this.miningLevel = miningLevel;
            this.LEVEL = level;
            this.speedModifier = miningSpeed;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Defensive");
            builder.push(LEVEL.toString());
            MANA_COST_ON_CAST = builder.comment("Mana cost on spell cast").defineInRange("mana_cost_on_cast", manaCostOnCast, 0, 10000);
            MINING_LEVEL = builder.comment("The mining level for the spell module.").<Tiers>defineEnum("item_level", miningLevel, Tiers.values());
            SPEED_MODIFIER = builder.comment("The speed that this module mines at, modifying its item level").defineInRange("speed_modifier",speedModifier, .1, 10);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
