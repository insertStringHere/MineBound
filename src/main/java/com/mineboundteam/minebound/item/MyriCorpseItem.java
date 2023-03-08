package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.entity.MyriCorpse;
import com.mineboundteam.minebound.entity.registry.EntityRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/***
 * Taken from ArmorStand, creates an item for spawning the corpse (For debug/creative mode)
 */
public class MyriCorpseItem extends Item{

    public MyriCorpseItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    // When using the item, spawn the corpse at the targeted block if possible
    public InteractionResult useOn(UseOnContext context) {
        Direction direction = context.getClickedFace();
        if (direction == Direction.DOWN) {
           return InteractionResult.FAIL;
        } else {
           Level level = context.getLevel();
           BlockPlaceContext blockplacecontext = new BlockPlaceContext(context);
           BlockPos blockpos = blockplacecontext.getClickedPos();
           ItemStack itemstack = context.getItemInHand();
           Vec3 vec3 = Vec3.atBottomCenterOf(blockpos);
           AABB aabb = EntityRegistry.MYRI_CORPSE.get().getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
           if (level.noCollision((Entity)null, aabb) && level.getEntities((Entity)null, aabb).isEmpty()) {
              if (level instanceof ServerLevel serverlevel) {
                 MyriCorpse block = EntityRegistry.MYRI_CORPSE.get().create(serverlevel, itemstack.getTag(), (Component)null, context.getPlayer(), blockpos, MobSpawnType.NATURAL, true, true);
                 if (block == null) {
                    return InteractionResult.FAIL;
                 }
  
                 float f = (float)Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                 block.moveTo(block.getX(), block.getY(), block.getZ(), f, 0.0F);
                 serverlevel.addFreshEntityWithPassengers(block);
                 level.playSound((Player)null, block.getX(), block.getY(), block.getZ(), SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                 level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, block);
              }
  
              itemstack.shrink(1);
              return InteractionResult.sidedSuccess(level.isClientSide);
           } else {
              return InteractionResult.FAIL;
           }
        }
     }
    
}