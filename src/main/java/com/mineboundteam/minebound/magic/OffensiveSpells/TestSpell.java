package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class TestSpell extends ActiveSpellItem {
    private int manaCost;

    public TestSpell(Properties properties, TestSpellConfig config) {
        super(properties, config.level);

        manaCost = config.MANA_COST.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                player.displayClientMessage(
                        new TextComponent(String.format("Player currently has %d mana", mana.getMana())), false);
            });

            super.reduceMana(manaCost, player);
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));

    }

    public static class TestSpellConfig implements IConfig {
        public IntValue MANA_COST;
        public final ArmorTier level;

        private int manaCost;

        public TestSpellConfig(int manaCost, ArmorTier tier) {
            this.manaCost = manaCost;
            this.level = tier;
        }

        @Override
        public void build(Builder builder) {
            builder.push("Test magic item");
            builder.push("Tier " + (level.getValue() + 1));
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {
        }
    }

}
