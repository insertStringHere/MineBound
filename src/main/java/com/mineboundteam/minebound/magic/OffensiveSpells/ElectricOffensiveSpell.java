package com.mineboundteam.minebound.magic.OffensiveSpells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.magic.events.MagicSelectedEvent;
import com.mineboundteam.minebound.magic.network.MagicSync;
import com.mineboundteam.minebound.util.MBDamageSources;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ElectricOffensiveSpell extends ActiveSpellItem {
    protected final ElectricOffensiveSpellConfig config;
    public static BooleanValue HURT_ALLIES;

    public ElectricOffensiveSpell(Properties properties, ElectricOffensiveSpellConfig config){
        super(properties, config.LEVEL, MagicType.ELECTRIC, SpellType.OFFENSIVE);
        this.config = config;
    }

    // keys:    playerID - low int
    //          hand key - top int [0 = main, 1 = off]
    // values:  array of linkages
    //              target entityID - low int
    //              src entityID - high int 
    protected static ConcurrentHashMap<Long, ArrayList<Long>> selectionTrees = new ConcurrentHashMap<>(20); 
    protected static final LightningBolt bolt = new LightningBolt(null, null);

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
    }


    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
        // Only do this every half a second
        if(level instanceof ServerLevel sLevel && tickCount % 10 == 0){
            // create use-instance key and closed/open list
            long key = combineLong(usedHand == InteractionHand.MAIN_HAND ? 0 : 1, player.getId());
            ArrayList<Long> zapLinks = new ArrayList<>();
            LinkedList<Tuple<LivingEntity, LivingEntity>> open = new LinkedList<>();
            
            // Pick out the currently looked-at mob. 
            if(player.pick(config.LIGHTING_STRETCH.get(), 0, false) instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof LivingEntity startEnt) {
                // And add it to the list, then add all mobs surrounding that one
                zapLinks.add(combineLong(player.getId(), startEnt.getId()));
                open.addAll(getSurroundingEntities(player, level));

                // Do a breadth first-best first search on that list
                while(open.size() > 0 && zapLinks.size() < config.LEAP_COUNT.get()){
                    // For each entity in it
                    Tuple<LivingEntity, LivingEntity> link = open.pop();
                    // Check that it has line of sight to the sourcing entity, and that it hasn't already been visited
                    if(link.getA().hasLineOfSight(link.getB()) && !containsEntity(zapLinks, link.getB()) && !(link.getB().getId() == player.getId())){
                        // If not, add it to the list, store its own surrounding entities, and then deal damage
                        zapLinks.add(combineLong(link.getA().getId(), link.getB().getId()));
                        open.addAll(getSurroundingEntities(link.getB(), level));
                        
                        link.getB().hurt(MBDamageSources.electrified(player), config.DAMAGE.get().floatValue());
                        if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(link.getB(), bolt))
                            link.getB().thunderHit(sLevel, bolt);
                    }
                }
            }
            // Finally, once either the spread has been exhausted or there's no surrounding entities
            selectionTrees.put(key, zapLinks);

            reduceMana(config.MANA_COST.get(), player);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        long key = combineLong(usedHand == InteractionHand.MAIN_HAND ? 0 : 1, player.getId());
        selectionTrees.remove(key);
    }    

    // Helper methods
    protected long combineLong(int high, int low){
        return ((long)high << 32) | low;
    }

    protected Collection<Tuple<LivingEntity, LivingEntity>> getSurroundingEntities(LivingEntity source, Level level){
        List<Entity> surrounding = level.getEntities(source, source.getBoundingBox().inflate(config.LIGHTING_STRETCH.get()));
        Collection<Tuple<LivingEntity, LivingEntity>> filtered = new PriorityQueue<>((a, b) -> (int)source.distanceTo(a.getB()));
        for(Entity e : surrounding){
            if(e instanceof LivingEntity entity && (!HURT_ALLIES.get() && e instanceof Mob m && m.isAggressive()))
                filtered.add(new Tuple<>(source, entity));
        }

        return filtered;
    }

    protected boolean containsEntity(ArrayList<Long> list, Entity e){
        for(long val : list){
            if((int)val == e.getId())
                return true;
        }
        return false;
    }
    
    // Events
    @SubscribeEvent
    public static void onSpellRemoved(MagicSelectedEvent.Remove event) {
        if (event.spell.getItem() instanceof ElectricOffensiveSpell spell) {
            spell.releaseUsing(event.spell, event.spellSlot.usedHand, event.player.level, event.player);
        }
    }

    @SubscribeEvent
    public static void worldTick(WorldTickEvent event){
        if(event.side == LogicalSide.SERVER){
            MagicSync.NET_CHANNEL.send(PacketDistributor.ALL.noArg(), new SpellRenderMsg(selectionTrees));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderLightning(RenderLevelStageEvent event){
        if(event.getStage() == Stage.AFTER_PARTICLES){
            var renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(bolt);
            // TODO: fancy calc here to render lightning bolt between entities.
            
            //renderer.render(bolt, MAX_STACK_SIZE, MAX_BAR_WIDTH, null, null, EAT_DURATION);
        }
    }
    
    // Config
    public static class ElectricOffensiveSpellConfig implements IConfig {
        public final ArmorTier LEVEL;

        public IntValue LIGHTING_STRETCH;
        public IntValue LEAP_COUNT;
        public DoubleValue DAMAGE;
        public IntValue MANA_COST;

        private final int leaps; 
        private final int stretch;
        private final int manacost;
        private final double damage;
        
        public ElectricOffensiveSpellConfig(int leaps, int stretch, int manacost, double damage, ArmorTier tier) {
            LEVEL = tier;
            this.leaps = leaps;
            this.stretch = stretch;
            this.manacost = manacost;
            this.damage = damage;
        }
            
        @Override
        public void build(Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost per half second of use").defineInRange("mana_cost", manacost, 0, 10000);
            DAMAGE = builder.comment("How much damage is dealt per half second").defineInRange("damage", damage, 0, 100);
            LEAP_COUNT = builder.comment("How many mobs can be connected by the lightning chain").defineInRange("leap_count", leaps, 0, 255);
            LIGHTING_STRETCH = builder.comment("How far apart mobs can be for the lightning to still connect").defineInRange("lighting_stretch", stretch, 1, 48);
        }

        @Override
        public void refresh(ModConfigEvent event) {
        }
        
    }

    // Network
    public static class SpellRenderMsg {
        public final ConcurrentHashMap<Long, ArrayList<Long>> selectionTree;

        public SpellRenderMsg(ConcurrentHashMap<Long, ArrayList<Long>> selectionTree){
            this.selectionTree = selectionTree;
        }
        public static void encode(SpellRenderMsg msg, FriendlyByteBuf buf) {
            buf.writeByte(selectionTrees.size());
            for (long key : selectionTrees.keySet()) {
                buf.writeLong(key);
                List<Long> vals = selectionTrees.get(key);
                buf.writeByte(vals.size());
                for(long val : vals)
                    buf.writeLong(val);
    
            }
        }
    
        public static SpellRenderMsg decode(FriendlyByteBuf buf) {
            int size = buf.readByte();
            ConcurrentHashMap<Long, ArrayList<Long>> tree = new ConcurrentHashMap<>(size);
    
            for(int i = 0; i < size; i++){
                long key = buf.readLong();
                int valSize = buf.readByte();
                ArrayList<Long> vals = new ArrayList<>(valSize);
                for(int j = 0; j < valSize; j++)
                    vals.add(buf.readLong());
                
                tree.put(key, vals);
            }
    
            return new SpellRenderMsg(tree);
        }     
    
        public static void handleSpellRecv(SpellRenderMsg msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork(() -> {
                selectionTrees = msg.selectionTree;
            });
            ctx.get().setPacketHandled(true);
        }
    }       
}
