package com.mineboundteam.minebound.config.registry;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.DefensiveSpells.EarthDefensiveSpell;
import com.mineboundteam.minebound.magic.OffensiveSpells.EnderOffensiveSpell;
import com.mineboundteam.minebound.magic.OffensiveSpells.ShieldOffensiveSpell;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import com.mineboundteam.minebound.magic.UtilitySpells.EarthUtilitySpell;
import com.mineboundteam.minebound.magic.UtilitySpells.ElectricUtilitySpell;
import com.mineboundteam.minebound.magic.UtilitySpells.ShieldUtilitySpell;
import com.mineboundteam.minebound.magic.UtilitySpells.TelekineticUtilitySpell;
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

    /* Telekinetic */
    public static final TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig TELEKINETIC_OFFENSIVE_1 = new TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig(50, 10, ArmorTier.EFFIGY);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_2 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(10, 0.20, false, ArmorTier.SUIT);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_3 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(7, 0.35, true, ArmorTier.SYNERGY);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_4 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(3, 0.50, true, ArmorTier.SINGULARITY);

    /* Shield */
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_1 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(30, 0.5, 0.4, ArmorTier.EFFIGY);
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_2 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(40, 0.7, 0.6, ArmorTier.SUIT);
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_3 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(50, 1.0, 0.8, ArmorTier.SYNERGY);
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

    /* Electric */
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_2 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(50, 1, false, ArmorTier.SUIT);
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_3 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(75, 2, true, ArmorTier.SYNERGY);
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_4 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(75, 3, true, ArmorTier.SINGULARITY);

    /* Light */

    /* Necrotic */

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
        EarthUtilitySpell.USE_TAGS = builder.comment("Whether or not the veinmining will use the minebound:tags/vein_mineable tag to choose whether or not a broken block will apply to veinmine.").comment("Turn off at risk of your own home.").define("use_tags", true);
        builder.pop();
        EARTH_UTILITY_2.build(builder);
        EARTH_UTILITY_3.build(builder);
        EARTH_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Ender");
        ENDER_OFFENSIVE_3.build(builder);
        builder.pop();

        builder.push("Electric");
        ELECTRIC_UTILITY_2.build(builder);
        ELECTRIC_UTILITY_3.build(builder);
        ELECTRIC_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Light");
        builder.pop();

        builder.push("Necrotic");
        builder.pop();
    }

    public void refresh(ModConfigEvent event) {
    }
}
