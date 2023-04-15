package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderOffensiveSpell extends ActiveSpellItem {
    private final int manaCost;
    private final int teleportDistance;

    public EnderOffensiveSpell(Properties properties, EnderOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.ENDER, SpellType.OFFENSIVE);

        this.manaCost = config.MANA_COST.get();
        this.teleportDistance = config.TP_DISTANCE.get();
    }

    @Override
    public void use(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            float direction = player.getYRot();
            double x = (0 - Math.sin(direction * Math.PI / 180f));
            double z = (Math.cos(direction * Math.PI / 180f));
            int distance = 1;
            while (distance <= teleportDistance) {
                BlockPos tpLocation = new BlockPos(player.getPosition(1).add(distance * x, 0, distance * z));
                // use isPossibleToRespawnInThis function to check if block has collision
                if (level.getBlockState(tpLocation).getBlock().isPossibleToRespawnInThis()) {
                    distance++;
                } else {
                    break;
                }
            }
            if (--distance > 0) {
                player.teleportTo(player.getX() + distance * x, player.getY(), player.getZ() + distance * z);
                reduceMana(manaCost, player);
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
        pTooltipComponents.add(new TextComponent("  - Teleports the player up to ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(teleportDistance + " blocks").withStyle(ChatFormatting.DARK_GREEN))
                                       .append(" in the direction they are looking"));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(manaCost + " Mana").withStyle(manaColorStyle))
                                       .append(" per teleport "));
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