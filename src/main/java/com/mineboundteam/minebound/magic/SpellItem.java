package com.mineboundteam.minebound.magic;

import java.util.List;
import java.util.stream.StreamSupport;

import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.mana.PlayerManaProvider;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class SpellItem extends Item {
    public SpellItem(Properties pProperties) {
        super(pProperties);
    }

    protected void reduceMana(int manaCost, Player p) {
        if (!p.level.isClientSide())
            p.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                // Charge player mana
                int underflow = mana.subtractMana(manaCost);

                // Reduce player's armor charge directly
                if (underflow > 0) {
                    List<ItemStack> armors = StreamSupport.stream(p.getArmorSlots().spliterator(), false)
                            .filter(slot -> slot.getItem() instanceof MyrialArmorItem)
                            .toList();

                    if (armors.size() != 0) {
                        int amnt = underflow / armors.size();
                        underflow = 0;

                        for (ItemStack armorItem : armors) {
                            int damage = armorItem.getItem().getDamage(armorItem) + amnt;
                            if (damage > armorItem.getItem().getMaxDamage(armorItem)) {
                                underflow += damage - armorItem.getItem().getMaxDamage(armorItem);
                                damage = armorItem.getItem().getMaxDamage(armorItem);
                            }

                            armorItem.getItem().setDamage(armorItem, damage);
                        }
                    }
                }

                // Reduce player health.
                if (underflow > 0) {
                    p.hurt(DamageSource.MAGIC, underflow/3);
                }
            });
    }

}
