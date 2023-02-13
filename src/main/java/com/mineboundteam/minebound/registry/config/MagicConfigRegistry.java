package com.mineboundteam.minebound.registry.config;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.OffensiveSpells.TestSpell;
import com.mineboundteam.minebound.magic.OffensiveSpells.ShieldOffensiveSpell;
import com.mineboundteam.minebound.magic.SpellLevel;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class MagicConfigRegistry implements IConfig {

    public static final TestSpell.TestSpellConfig TEST_SPELL = new TestSpell.TestSpellConfig(10, ArmorTier.EFFIGY);

    /* Shield */
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_1 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(30, SpellLevel.Level1);
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_2 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(40, SpellLevel.Level2);
    public static final ShieldOffensiveSpell.ShieldOffensiveSpellConfig SHIELD_OFFENSIVE_3 = new ShieldOffensiveSpell.ShieldOffensiveSpellConfig(50, SpellLevel.Level3);

    @Override
    public void build(Builder builder) {
        TEST_SPELL.build(builder);

        /* Shield */
        SHIELD_OFFENSIVE_1.build(builder);
        SHIELD_OFFENSIVE_2.build(builder);
        SHIELD_OFFENSIVE_3.build(builder);
    }

    @Override
    public void refresh(ModConfigEvent event) {
    }
    
}
