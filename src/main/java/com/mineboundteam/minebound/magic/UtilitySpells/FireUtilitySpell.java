package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.network.chat.Component;
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

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class FireUtilitySpell extends PassiveSpellItem {
    private final int manaCost;
    private final int aoeRange;
    private final int damageRate;
    private final double ignitionChance;
    private final int ignitionDamage;


    public FireUtilitySpell(Properties properties, FireUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.FIRE, SpellType.UTILITY);

        this.manaCost = config.MANA_COST.get();
        this.aoeRange = config.AOE_RANGE.get();
        this.damageRate = config.DAMAGE_RATE.get();
        this.ignitionChance = config.IGNITION_CHANCE.get();
        this.ignitionDamage = config.IGNITION_DAMAGE.get();
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            List<FireUtilitySpell> equippedSpells = getEquippedSpellItemsOfType(FireUtilitySpell.class, player);
            if(equippedSpells.size() > 0) {
                // do shit
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static class FireUtilitySpellConfig implements IConfig {
        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue AOE_RANGE;
        public ForgeConfigSpec.IntValue DAMAGE_RATE;
        public ForgeConfigSpec.DoubleValue IGNITION_CHANCE;
        public ForgeConfigSpec.IntValue IGNITION_DAMAGE;
        public final ArmorTier LEVEL;

        private final int manaCost;
        private final int aoeRange;
        private final int damageRate;
        private final double ignitionChance;
        private final int ignitionDamage;

        public FireUtilitySpellConfig(int manaCost, int aoeRange, int damageRate, double ignitionChance, int ignitionDamage, ArmorTier level) {
            this.manaCost = manaCost;
            this.aoeRange = aoeRange;
            this.damageRate = damageRate;
            this.ignitionChance = ignitionChance;
            this.ignitionDamage = ignitionDamage;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost per application of damage").defineInRange("mana_cost", manaCost, 0, 10000);
            AOE_RANGE = builder.comment("Area of effect in blocks").defineInRange("aoe_range", aoeRange, 0, 10000);
            DAMAGE_RATE = builder.comment("How often in ticks the spell will damage entities in range (20 ticks = 1 second)").defineInRange("damage_rate", damageRate, 0, 10000);
            IGNITION_CHANCE = builder.comment("How likely it is for an entity to be lit on fire every second").defineInRange("ignition_chance", ignitionChance, 0.0, 1.0);
            IGNITION_DAMAGE = builder.comment("How much damage the ignition does per damage tick").defineInRange("ignition_damage", ignitionDamage, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
