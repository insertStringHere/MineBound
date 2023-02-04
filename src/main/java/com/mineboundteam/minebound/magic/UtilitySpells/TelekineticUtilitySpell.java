package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellLevel;
import com.mineboundteam.minebound.mana.PlayerManaProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class TelekineticUtilitySpell extends PassiveSpellItem {

    private final int manaCost;
    private final SpellLevel level;

    public TelekineticUtilitySpell(Properties properties, TelekineticUtilitySpellConfig config) {
        super(properties);

        manaCost = config.MANA_COST.get();
        level = config.LEVEL;
    }

    @Override
    public void updateEquipment(MyrialArmorItem item, Player player) {
        super.updateEquipment(item, player);
        //TODO: check if spell is equipped in a utility slot, then do this
        player.getAbilities().mayfly = !player.getAbilities().mayfly;
        player.onUpdateAbilities();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.player.level.getDayTime() % 20 == 0 && event.phase == TickEvent.Phase.START && event.player.getAbilities().mayfly) {
            event.player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                if (event.player.getAbilities().flying) {
                    super.reduceMana(manaCost, event.player);
                }
            });
        }
    }

    public static class TelekineticUtilitySpellConfig implements IConfig {

        public IntValue MANA_COST;
        public final SpellLevel LEVEL;

        private final int manaCost;

        public TelekineticUtilitySpellConfig(int manaCost, SpellLevel level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
        }

        @Override
        public void build(Builder builder) {
            builder.push("Telekinetic Utility");
            MANA_COST = builder.comment("Tier " + LEVEL.getValue() + " Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop();
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
