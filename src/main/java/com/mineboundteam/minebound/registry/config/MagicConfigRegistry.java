package com.mineboundteam.minebound.registry.config;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.magic.OffensiveSpells.TestSpell;
import com.mineboundteam.minebound.magic.SpellLevel;
import com.mineboundteam.minebound.magic.UtilitySpells.TelekineticUtilitySpell;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class MagicConfigRegistry implements IConfig {

    public static final TestSpell.TestSpellConfig TEST_SPELL = new TestSpell.TestSpellConfig(10);
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_2 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(10, SpellLevel.Level2);

    @Override
    public void build(Builder builder) {
        TEST_SPELL.build(builder);
        TELEKINETIC_UTILITY_2.build(builder);
    }

    @Override
    public void refresh(ModConfigEvent event) {
    }

}
