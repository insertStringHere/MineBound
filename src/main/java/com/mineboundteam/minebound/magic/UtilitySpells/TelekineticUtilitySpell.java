package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;


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
            if (event.player.getAbilities().flying) {
                super.reduceMana(manaCost, event.player);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("While equipped in utility slot:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - Gives creative flight").withStyle(ChatFormatting.GRAY));
//        pTooltipComponents.add(new TextComponent("  - Gives elytra flight"));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       // TODO: Color subject to change once mana UI is implemented
                                       .append(new TextComponent(manaCost + " Mana").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE))
                                       .append(" per second of flight").withStyle(ChatFormatting.GRAY));
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
