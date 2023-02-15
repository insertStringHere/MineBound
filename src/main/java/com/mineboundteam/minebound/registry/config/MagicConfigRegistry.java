package com.mineboundteam.minebound.registry.config;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.OffensiveSpells.ShieldOffensiveSpell;
import com.mineboundteam.minebound.magic.OffensiveSpells.TestSpell;
import com.mineboundteam.minebound.magic.UtilitySpells.TelekineticUtilitySpell;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class MagicConfigRegistry implements IConfig {

    public static final TestSpell.TestSpellConfig TEST_SPELL = new TestSpell.TestSpellConfig(10, ArmorTier.EFFIGY);

    /*
     * Spell Element
     * [offensive spells]
     * [defensive spells]
     * [utility spells]
     */

    /* Fire */

    /* Telekinetic */
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_2 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(10, 0.20, ArmorTier.SUIT);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_3 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(7, 0.35, ArmorTier.SYNERGY);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_4 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(3, 0.50, ArmorTier.SINGULARITY);

    /* Shield */
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_1 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(30, 0.5, 0.4, ArmorTier.EFFIGY);
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_2 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(40, 0.7, 0.6, ArmorTier.SUIT);
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_3 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(50, 1.0, 0.8, ArmorTier.SYNERGY);

    /* Earth */

    /* Ender */

    /* Electric */

    /* Light */

    /* Necrotic */


    @Override
    public void build(Builder builder) {
        builder.push("Test");
        TEST_SPELL.build(builder);
        builder.pop();

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
        TELEKINETIC_UTILITY_2.build(builder);
        TELEKINETIC_UTILITY_3.build(builder);
        TELEKINETIC_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Shield");
        SHIELD_OFFENSIVE_1.build(builder);
        SHIELD_OFFENSIVE_2.build(builder);
        SHIELD_OFFENSIVE_3.build(builder);
        builder.pop();

        builder.push("Earth");
        builder.pop();

        builder.push("Ender");
        builder.pop();

        builder.push("Electric");
        builder.pop();

        builder.push("Light");
        builder.pop();

        builder.push("Necrotic");
        builder.pop();
    }

    @Override
    public void refresh(ModConfigEvent event) {
    }
}
