package com.mineboundteam.minebound.config.registry;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.DefensiveSpells.*;
import com.mineboundteam.minebound.magic.OffensiveSpells.*;
import com.mineboundteam.minebound.magic.UtilitySpells.*;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.io.File;

public class MagicConfigRegistry extends ServerConfigRegistry implements IConfig {
    private static ServerConfigRegistry registry;

    public MagicConfigRegistry() {
        this.configName = "MineBound" + File.separator + "Spells.toml";
        this.addConfig(this);
    }

    public static ServerConfigRegistry get() {
        if (registry == null) {
            registry = new MagicConfigRegistry();
        }
        return registry;
    }

    /*
     * Spell Element
     * [offensive spells]
     * [defensive spells]
     * [utility spells]
     */

    /* Fire */
    public static final FireOffensiveSpell.FireOffensiveSpellConfig FIRE_OFFENSIVE_1 = new FireOffensiveSpell.FireOffensiveSpellConfig(10, 8, 5.0, false, false, 0, ArmorTier.EFFIGY);
    public static final FireOffensiveSpell.FireOffensiveSpellConfig FIRE_OFFENSIVE_2 = new FireOffensiveSpell.FireOffensiveSpellConfig(20, 16, 10.0, true, false, 0, ArmorTier.SUIT);
    public static final FireOffensiveSpell.FireOffensiveSpellConfig FIRE_OFFENSIVE_4 = new FireOffensiveSpell.FireOffensiveSpellConfig(30, 16, 10.0, true, true, 50, ArmorTier.SINGULARITY);
    public static final FireDefensiveSpell.FireDefensiveSpellConfig FIRE_DEFENSIVE_1 = new FireDefensiveSpell.FireDefensiveSpellConfig(35, ArmorTier.EFFIGY);
    public static final FireUtilitySpell.FireUtilitySpellConfig FIRE_UTILITY_2 = new FireUtilitySpell.FireUtilitySpellConfig(5, 25, 10, 1.0, 40, ArmorTier.SUIT);
    public static final FireUtilitySpell.FireUtilitySpellConfig FIRE_UTILITY_3 = new FireUtilitySpell.FireUtilitySpellConfig(7, 25, 15, 2.0, 20, ArmorTier.SYNERGY);
    public static final FireUtilitySpell.FireUtilitySpellConfig FIRE_UTILITY_4 = new FireUtilitySpell.FireUtilitySpellConfig(10, 25, 20, 3.0, 20, ArmorTier.SINGULARITY);

    /* Telekinetic */
    public static final TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig TELEKINETIC_OFFENSIVE_1 = new TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig(50, 10, ArmorTier.EFFIGY);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_2 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(30, 0.20, false, ArmorTier.SUIT);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_3 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(25, 0.35, false, ArmorTier.SYNERGY);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_4 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(25, 0.50, true, ArmorTier.SINGULARITY);

    /* Shield */
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_1 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(30, 0.5, 0.4, ArmorTier.EFFIGY);
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_2 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(40, 0.7, 0.6, ArmorTier.SUIT);
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_3 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(50, 1.0, 0.8, ArmorTier.SYNERGY);
    public static final ShieldDefensiveSpell.ShieldDefensiveSpellConfig SHIELD_DEFENSIVE_1 = new ShieldDefensiveSpell.ShieldDefensiveSpellConfig(20, ArmorTier.EFFIGY);
    public static final ShieldDefensiveSpell.ShieldDefensiveSpellConfig SHIELD_DEFENSIVE_2 = new ShieldDefensiveSpell.ShieldDefensiveSpellConfig(10, ArmorTier.SUIT);
    public static final ShieldDefensiveSpell.ShieldDefensiveSpellConfig SHIELD_DEFENSIVE_3 = new ShieldDefensiveSpell.ShieldDefensiveSpellConfig(5, ArmorTier.SYNERGY);
    public static final ShieldUtilitySpell.ShieldUtilitySpellConfig SHIELD_UTILITY_2 = new ShieldUtilitySpell.ShieldUtilitySpellConfig(20, 20, 200, ArmorTier.SUIT);
    public static final ShieldUtilitySpell.ShieldUtilitySpellConfig SHIELD_UTILITY_3 = new ShieldUtilitySpell.ShieldUtilitySpellConfig(20, 40, 140, ArmorTier.SYNERGY);
    public static final ShieldUtilitySpell.ShieldUtilitySpellConfig SHIELD_UTILITY_4 = new ShieldUtilitySpell.ShieldUtilitySpellConfig(10, 40, 100, ArmorTier.SINGULARITY);

    /* Earth */

    public static final EarthDefensiveSpell.EarthDefensiveSpellConfig EARTH_DEFENSIVE_1 = new EarthDefensiveSpell.EarthDefensiveSpellConfig(10, Tiers.STONE, 4.0F, ArmorTier.EFFIGY);
    public static final EarthDefensiveSpell.EarthDefensiveSpellConfig EARTH_DEFENSIVE_2 = new EarthDefensiveSpell.EarthDefensiveSpellConfig(10, Tiers.IRON, 6.0F, ArmorTier.SUIT);
    public static final EarthDefensiveSpell.EarthDefensiveSpellConfig EARTH_DEFENSIVE_3 = new EarthDefensiveSpell.EarthDefensiveSpellConfig(10, Tiers.DIAMOND, 9.0F, ArmorTier.SYNERGY);
    public static final EarthDefensiveSpell.EarthDefensiveSpellConfig EARTH_DEFENSIVE_4 = new EarthDefensiveSpell.EarthDefensiveSpellConfig(10, Tiers.NETHERITE, 10.0f, ArmorTier.SINGULARITY);
    public static final EarthUtilitySpell.EarthUtilitySpellConfig EARTH_UTILITY_2 = new EarthUtilitySpell.EarthUtilitySpellConfig(10, 15, 1, 8, ArmorTier.SUIT);
    public static final EarthUtilitySpell.EarthUtilitySpellConfig EARTH_UTILITY_3 = new EarthUtilitySpell.EarthUtilitySpellConfig(15, 30, 1, 16, ArmorTier.SYNERGY);
    public static final EarthUtilitySpell.EarthUtilitySpellConfig EARTH_UTILITY_4 = new EarthUtilitySpell.EarthUtilitySpellConfig(20, 40, 2, 32, ArmorTier.SINGULARITY);

    /* Ender */
    public static final EnderOffensiveSpell.EnderOffensiveSpellConfig ENDER_OFFENSIVE_3 = new EnderOffensiveSpell.EnderOffensiveSpellConfig(100, 15, ArmorTier.SYNERGY);
    public static final EnderDefensiveSpell.EnderDefensiveSpellConfig ENDER_DEFENSIVE_1 = new EnderDefensiveSpell.EnderDefensiveSpellConfig(20, 15, 40, ArmorTier.SUIT);
    public static final EnderDefensiveSpell.EnderDefensiveSpellConfig ENDER_DEFENSIVE_3 = new EnderDefensiveSpell.EnderDefensiveSpellConfig(40, 20, 80, ArmorTier.SYNERGY);
    public static final EnderDefensiveSpell.EnderDefensiveSpellConfig ENDER_DEFENSIVE_4 = new EnderDefensiveSpell.EnderDefensiveSpellConfig(50, 25, 160, ArmorTier.SINGULARITY);

    public static final EnderUtilitySpell.EnderUtilitySpellConfig ENDER_UTILITY_3 = new EnderUtilitySpell.EnderUtilitySpellConfig(100,ArmorTier.SYNERGY);

    /* Electric */
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_2 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(50, 1, false, ArmorTier.SUIT);
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_3 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(75, 2, true, ArmorTier.SYNERGY);
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_4 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(75, 3, true, ArmorTier.SINGULARITY);

    /* Light */
    public static final LightDefensiveSpell.LightDefensiveSpellConfig LIGHT_DEFENSIVE_1 = new LightDefensiveSpell.LightDefensiveSpellConfig(25, true, 600, ArmorTier.EFFIGY);
    public static final LightUtilitySpell.LightUtilitySpellConfig LIGHT_UTILITY_2 = new LightUtilitySpell.LightUtilitySpellConfig(150, 40, ArmorTier.SUIT);
    public static final LightUtilitySpell.LightUtilitySpellConfig LIGHT_UTILITY_3 = new LightUtilitySpell.LightUtilitySpellConfig(200, 85, ArmorTier.SYNERGY);
    public static final LightUtilitySpell.LightUtilitySpellConfig LIGHT_UTILITY_4 = new LightUtilitySpell.LightUtilitySpellConfig(250, 120, ArmorTier.SINGULARITY);

    /* Necrotic */
    public static final NecroticOffensiveSpell.NecroticOffensiveSpellConfig NECROTIC_OFFENSIVE_2 = new NecroticOffensiveSpell.NecroticOffensiveSpellConfig(20, .50, .10, ArmorTier.SUIT);
    public static final NecroticOffensiveSpell.NecroticOffensiveSpellConfig NECROTIC_OFFENSIVE_3 = new NecroticOffensiveSpell.NecroticOffensiveSpellConfig(25, 1.25, .20, ArmorTier.SYNERGY);
    public static final NecroticOffensiveSpell.NecroticOffensiveSpellConfig NECROTIC_OFFENSIVE_4 = new NecroticOffensiveSpell.NecroticOffensiveSpellConfig(30, 2.50, .30, ArmorTier.SINGULARITY);

    public void build(Builder builder) {
        /*
         * Spell Element
         * [offensive spells]
         * [defensive spells]
         * [utility spells]
         * Follow this ordering to ensure config is generated the same for all spells
         */

        builder.push("Fire");
        builder.pop();

        builder.push("Fire");
            FIRE_OFFENSIVE_1.build(builder);
            FIRE_OFFENSIVE_2.build(builder);
            FIRE_OFFENSIVE_4.build(builder);
            FIRE_DEFENSIVE_1.build(builder);
            FIRE_UTILITY_2.build(builder);
            FIRE_UTILITY_3.build(builder);
            FIRE_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Telekinetic");
            TELEKINETIC_OFFENSIVE_1.build(builder);
            TELEKINETIC_UTILITY_2.build(builder);
            TELEKINETIC_UTILITY_3.build(builder);
            TELEKINETIC_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Shield");
            SHIELD_OFFENSIVE_1.build(builder);
            SHIELD_OFFENSIVE_2.build(builder);
            SHIELD_OFFENSIVE_3.build(builder);
            SHIELD_DEFENSIVE_1.build(builder);
            SHIELD_DEFENSIVE_2.build(builder);
            SHIELD_DEFENSIVE_3.build(builder);
            SHIELD_UTILITY_2.build(builder);
            SHIELD_UTILITY_3.build(builder);
            SHIELD_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Earth");
            builder.push("Defensive");
                EarthDefensiveSpell.vanillaBreak = builder.comment("True if breaking blocks should take time, like when using a pickaxe.").define("vanilla_break", true);
            builder.pop();
            EARTH_DEFENSIVE_1.build(builder);
            EARTH_DEFENSIVE_2.build(builder);
            EARTH_DEFENSIVE_3.build(builder);
            EARTH_DEFENSIVE_4.build(builder);
            builder.push("Utility");
                EarthUtilitySpell.TOLERANCE = builder.comment("How many blocks without an ore will be accepted as part of the same vein before giving up.").defineInRange("tolerance", 2, 1, 10);
                EarthUtilitySpell.USE_TAGS = builder.comment("Whether or not the vein mining will use the minebound:tags/vein_mineable tag to choose whether or not a broken block will apply to veinmine.").comment("Turn off at risk of your own home.").define("use_tags", true);
            builder.pop();
            EARTH_UTILITY_2.build(builder);
            EARTH_UTILITY_3.build(builder);
            EARTH_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Ender");
            ENDER_OFFENSIVE_3.build(builder);
            ENDER_DEFENSIVE_1.build(builder);
            ENDER_DEFENSIVE_3.build(builder);
            ENDER_DEFENSIVE_4.build(builder);
            ENDER_UTILITY_3.build(builder);
        builder.pop();

        builder.push("Electric");
            ELECTRIC_UTILITY_2.build(builder);
            ELECTRIC_UTILITY_3.build(builder);
            ELECTRIC_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Light");
            LIGHT_DEFENSIVE_1.build(builder);
            LIGHT_UTILITY_2.build(builder);
            LIGHT_UTILITY_3.build(builder);
            LIGHT_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Necrotic");
            builder.push("Offensive");
                NecroticOffensiveSpell.FOOD_REDUCTION = builder.comment("The percent reduction from healing to replenishing food while using Necrotic Offensive.").comment("Will always recover at least 1 hunger if applicable.").defineInRange("food_reduction", .80, .01, 10);
            builder.pop();
            NECROTIC_OFFENSIVE_2.build(builder);
            NECROTIC_OFFENSIVE_3.build(builder);
            NECROTIC_OFFENSIVE_4.build(builder);
        builder.pop();
    }

    public void refresh(ModConfigEvent event) {
    }
}
