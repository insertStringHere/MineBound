package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class EnderUtilitySpell extends PassiveSpellItem {
    private final int manaReduction;

    public EnderUtilitySpell(Properties properties, EnderUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.ENDER, SpellType.UTILITY);

        this.manaReduction = config.MANA_REDUCTION.get();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            EnderUtilitySpell spell = getHighestSpellItem(EnderUtilitySpell.class, player);
            if (spell != null) {
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("ender_utility", -spell.manaReduction));
                player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(utilityToggle -> {
                    if (utilityToggle.ender) {
                        // Has to be longer than 10 seconds otherwise it flickers horribly
                        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 205, 0, false, false));
                    }
                });
            } else {
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("ender_utility", 0));
            }
        }
    }

    @Override
    @SuppressWarnings("resource")
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(enabledHeader);
        pTooltipComponents.add(new TextComponent("    - Gives ").withStyle(defaultColor)
                .append(new TextComponent("Night Vision").withStyle(Style.EMPTY.withColor(MobEffects.NIGHT_VISION.getColor()))));
        pTooltipComponents.add(new TextComponent("Reduces ").withStyle(defaultColor)
                .append(new TextComponent("Manapool").withStyle(manaColorStyle))
                .append(" by ").append(new TextComponent(manaReduction + "").withStyle(reductionColorStyle)));

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Optional<PlayerUtilityToggleProvider.UtilityToggle> toggle = player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).resolve();
            if (toggle.isPresent() && toggle.get().ender)
                pTooltipComponents.add(enabled);
            else
                pTooltipComponents.add(disabled);
        }
    }

    public static class EnderUtilitySpellConfig implements IConfig {
        public ForgeConfigSpec.IntValue MANA_REDUCTION;
        public final ArmorTier LEVEL;

        private final int manaReduction;

        public EnderUtilitySpellConfig(int manaReduction, ArmorTier level) {
            this.manaReduction = manaReduction;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_REDUCTION = builder.comment("How much total mana will be reduced by").defineInRange("mana_reduction", manaReduction, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
