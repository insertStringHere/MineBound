package com.mineboundteam.minebound.item.tool;

import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import net.minecraft.world.item.Tier;


public class MyrialMachete extends MyrialSwordItem {
    public MyrialMachete(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, config);
    }
}
