package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellLevel;
import com.mineboundteam.minebound.mana.PlayerManaProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class TelekineticUtilitySpell extends PassiveSpellItem {

    private final int manaCost;

    public TelekineticUtilitySpell(Properties properties, SpellLevel level, TelekineticUtilitySpellConfig config) {
        super(properties, level);

        manaCost = config.MANA_COST.get();
    }

    @Override
    public void updateEquipment(MyrialArmorItem item, Player player) {
        super.updateEquipment(item, player);

        // This shit definitely ain't working
        if (player.getAbilities().flying) {
            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> player.displayClientMessage(
                    new TextComponent(String.format("Player currently has %d mana", mana.getMana())), false));

            super.reduceMana(manaCost, player);
        }
    }

    public static class TelekineticUtilitySpellConfig implements IConfig {

        public IntValue MANA_COST;

        private final int manaCost;
        private final SpellLevel level;

        public TelekineticUtilitySpellConfig(int manaCost, SpellLevel level) {
            this.manaCost = manaCost;
            this.level = level;
        }

        @Override
        public void build(Builder builder) {
            builder.push("Telekinetic Utility");
            MANA_COST = builder.comment("Tier " + level.getValue() + " Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop();
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
