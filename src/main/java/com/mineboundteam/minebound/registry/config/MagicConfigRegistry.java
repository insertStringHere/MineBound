package com.mineboundteam.minebound.registry.config;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.OffensiveSpells.FireOffensiveSpell;
import com.mineboundteam.minebound.magic.OffensiveSpells.TestSpell;
import com.mineboundteam.minebound.magic.UtilitySpells.ShieldUtilitySpell;
import com.mineboundteam.minebound.magic.UtilitySpells.TelekineticUtilitySpell;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class MagicConfigRegistry implements IConfig {

    public static final TestSpell.TestSpellConfig TEST_SPELL = new TestSpell.TestSpellConfig(10, ArmorTier.EFFIGY);

    /* Fire */
    public static final FireOffensiveSpell.FireOffensiveSpellConfig FIRE_OFFENSIVE_1 = new FireOffensiveSpell.FireOffensiveSpellConfig(10, ArmorTier.EFFIGY);

    /* Telekinetic */
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_2 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(10, 0.20, ArmorTier.SUIT);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_3 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(7, 0.35, ArmorTier.SYNERGY);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_4 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(3, 0.50, ArmorTier.SINGULARITY);

    /* Shield */
    public static final ShieldUtilitySpell.ShieldUtilitySpellConfig SHIELD_UTILITY_2 = new ShieldUtilitySpell.ShieldUtilitySpellConfig(20, 20, 200, ArmorTier.SUIT);
    public static final ShieldUtilitySpell.ShieldUtilitySpellConfig SHIELD_UTILITY_3 = new ShieldUtilitySpell.ShieldUtilitySpellConfig(20, 40, 140, ArmorTier.SYNERGY);
    public static final ShieldUtilitySpell.ShieldUtilitySpellConfig SHIELD_UTILITY_4 = new ShieldUtilitySpell.ShieldUtilitySpellConfig(10, 40, 100, ArmorTier.SINGULARITY);

    @Override
    public void build(Builder builder) {
        builder.push("Test");
        TEST_SPELL.build(builder);
        builder.pop();

        builder.push("Fire");
        FIRE_OFFENSIVE_1.build(builder);
        builder.pop();

        builder.push("Telekinetic");
        TELEKINETIC_UTILITY_2.build(builder);
        TELEKINETIC_UTILITY_3.build(builder);
        TELEKINETIC_UTILITY_4.build(builder);
        builder.pop();

        builder.push("Shield");
        SHIELD_UTILITY_2.build(builder);
        SHIELD_UTILITY_3.build(builder);
        SHIELD_UTILITY_4.build(builder);
        builder.pop();
    }

    @Override
    public void refresh(ModConfigEvent event) {
    }

}
