package com.mineboundteam.minebound.config.registry;

import java.io.File;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.DefensiveSpells.EarthDefensiveSpell;
import com.mineboundteam.minebound.magic.DefensiveSpells.LightDefensiveSpell;
import com.mineboundteam.minebound.magic.OffensiveSpells.ShieldOffensiveSpell;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import com.mineboundteam.minebound.magic.UtilitySpells.ElectricUtilitySpell;
import com.mineboundteam.minebound.magic.UtilitySpells.ShieldUtilitySpell;
import com.mineboundteam.minebound.magic.UtilitySpells.TelekineticUtilitySpell;

import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class MagicConfigRegistry extends ServerConfigRegistry implements IConfig{
    private static ServerConfigRegistry registry;
    public MagicConfigRegistry(){
        this.configName = "MineBound" + File.separator + "Spells.toml";
        this.addConfig(this);
    }
    public static ServerConfigRegistry get(){
        if(registry == null){
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

    /* Ender */

    /* Electric */
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_2 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(50, 1, false, ArmorTier.SUIT);
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_3 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(75, 2, true, ArmorTier.SYNERGY);
    public static final ElectricUtilitySpell.ElectricUtilitySpellConfig ELECTRIC_UTILITY_4 = new ElectricUtilitySpell.ElectricUtilitySpellConfig(75, 3, true, ArmorTier.SINGULARITY);

    /* Light */
    public static final LightDefensiveSpell.LightDefensiveSpellConfig LIGHT_DEFENSIVE_1 = new LightDefensiveSpell.LightDefensiveSpellConfig(25, ArmorTier.EFFIGY);

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
        builder.pop();

        builder.push("Ender");
        builder.pop();

        builder.push("Electric");
        ELECTRIC_UTILITY_2.build(builder);
        ELECTRIC_UTILITY_3.build(builder);
        ELECTRIC_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Light");
        LIGHT_DEFENSIVE_1.build(builder);
        builder.pop();

        builder.push("Necrotic");
        builder.pop();
    }

    public void refresh(ModConfigEvent event) {
    }
}
