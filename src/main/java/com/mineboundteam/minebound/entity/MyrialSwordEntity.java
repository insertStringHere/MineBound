package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class MyrialSwordEntity extends MyrialSwordEntityBase {

    public MyrialSwordEntity(EntityType<MyrialSwordEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MyrialSwordEntity(Player player, Level level, InteractionHand usedHand, Predicate<ItemStack> isPlaceholder,
                             TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
        super(EntityRegistry.MYRIAL_SWORD_ENTITY.get(), player, level, usedHand, isPlaceholder, config);
    }
}
