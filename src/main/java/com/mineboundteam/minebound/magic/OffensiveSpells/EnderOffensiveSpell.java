package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderOffensiveSpell extends ActiveSpellItem {
    private final EnderOffensiveSpellConfig config;

    public EnderOffensiveSpell(Properties properties, EnderOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.ENDER, SpellType.OFFENSIVE);

        this.config = config;
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        if (!level.isClientSide()) {
            BlockHitResult result = (BlockHitResult) player.pick(config.TP_DISTANCE.get(), 1f, false);
            BlockPos pos = result.getBlockPos().relative(result.getDirection());
            double dX = Math.abs(player.getX() - pos.getX() - 0.5);
            double dY = Math.abs(player.getY() - pos.getY());
            double dZ = Math.abs(player.getZ() - pos.getZ() - 0.5);
            // Only execute if player would move > 1 block
            if (Math.floor(dX + dY + dZ) > 0) {
                player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                level.playSound(null, player, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
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
        pTooltipComponents.add(new TextComponent("  - Teleports the player up to ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.pluralize(config.TP_DISTANCE.get(), "block")).withStyle(ColorUtil.Tooltip.timeAndDistanceColor))
                .append(" in the direction they are looking"));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get(), " per teleport"));
    }

    public static class EnderOffensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue TP_DISTANCE;
        public final ArmorTier LEVEL;
        private final int manaCost;
        private final int teleportDistance;

        public EnderOffensiveSpellConfig(int manaCost, int teleportDistance, ArmorTier level) {
            this.manaCost = manaCost;
            this.teleportDistance = teleportDistance;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            TP_DISTANCE = builder.comment("Maximum teleport distance").defineInRange("tp_dist", teleportDistance, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
