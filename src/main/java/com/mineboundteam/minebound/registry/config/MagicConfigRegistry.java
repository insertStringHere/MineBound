package com.mineboundteam.minebound.registry.config;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.OffensiveSpells.TestSpell;
import com.mineboundteam.minebound.magic.UtilitySpells.TelekineticUtilitySpell;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class MagicConfigRegistry implements IConfig {

    public static final TestSpell.TestSpellConfig TEST_SPELL = new TestSpell.TestSpellConfig(10, ArmorTier.EFFIGY);

    /* Telekinetic */
    public static final TelekineticUtilitySpell.TelekineticUtilitySpellConfig TELEKINETIC_UTILITY_2 = new TelekineticUtilitySpell.TelekineticUtilitySpellConfig(10, ArmorTier.SUIT);

    @Override
    public void build(Builder builder) {
        TEST_SPELL.build(builder);
        builder.pop();

        /* Telekinetic */
        TELEKINETIC_UTILITY_2.build(builder);
        builder.pop();
    }

    @Override
    public void refresh(ModConfigEvent event) {
    }

}
