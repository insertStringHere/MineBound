package com.mineboundteam.minebound.registry.config;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.OffensiveSpells.TestSpell;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class MagicConfigRegistry implements IConfig {

    public static final TestSpell.TestSpellConfig TEST_SPELL = new TestSpell.TestSpellConfig(10, ArmorTier.EFFIGY);

    @Override
    public void build(Builder builder) {
        TEST_SPELL.build(builder);
    }

    @Override
    public void refresh(ModConfigEvent event) {
    }
    
}
