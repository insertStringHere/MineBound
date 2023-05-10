package com.mineboundteam.minebound.magic.DefensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.magic.events.MagicSelectedEvent;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class TelekineticDefensiveSpell extends ActiveSpellItem {

    protected final TelekineticDefensiveSpellConfig config;

    public TelekineticDefensiveSpell(Properties properties, TelekineticDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.TELEKINETIC, SpellType.DEFENSIVE);
        this.config = config;
    }

    protected static final ConcurrentHashMap<Integer, Integer> selectedMap = new ConcurrentHashMap<>();
    protected static final ConcurrentHashMap<Integer, InteractionHand> attackMap = new ConcurrentHashMap<>();

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        if (!level.isClientSide()) {
            Integer id = selectedMap.get(player.getId());
            if (id != null) {
                Entity entity = level.getEntity(id);
                if (entity != null) {
                    attackMap.put(player.getId(), usedHand);
                    if (entity.hurt(DamageSource.playerAttack(player).setIsFall(), (float) (config.DAMAGE.get().doubleValue())))
                        reduceMana((int) (config.MANA_COST.get() * 10), player);
                }
            } else {
                Vec3 start = player.getEyePosition();
                Vec3 end = player.getViewVector(0).scale(config.GRAB_DISTANCE.get()).add(player.getEyePosition());
                EntityHitResult entity = ProjectileUtil.getEntityHitResult(level, player, start, end, (new AABB(start, end)).inflate(1), e -> !e.isSpectator(), 1);
                if (entity != null && entity.getEntity() instanceof LivingEntity living) {
                    selectedMap.put(player.getId(), living.getId());
                }
            }
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
        Integer id = selectedMap.get(player.getId());
        InteractionHand cacheHand = attackMap.get(player.getId());
        if (!level.isClientSide() && id != null && (cacheHand == null || cacheHand != usedHand)) {
            Entity e = level.getEntity(id);
            if (e != null && e.isAlive() && e instanceof LivingEntity entity) {
                double velocity = entity.getDeltaMovement().lengthSqr();
                reduceMana((int) (Math.ceil(velocity * 100 * config.MANA_COST.get())), player);

                entity.move(MoverType.PLAYER, getShift(player, usedHand, entity));
                entity.resetFallDistance();

                // Handle collision damage
                // Kinda glitchy, keep off by default
                if (config.DO_DAMAGE.get()) {
                    float dmg = (float) (entity.getDeltaMovement().lengthSqr() * 100 * config.DAMAGE.get());
                    if (dmg >= 1 && (entity.horizontalCollision || entity.verticalCollision)) {
                        entity.hurt(DamageSource.playerAttack(player).setIsFall(), dmg);
                    }
                }

                entity.setDeltaMovement(Vec3.ZERO);
            } else {
                selectedMap.remove(player.getId());
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        InteractionHand cacheHand = attackMap.remove(player.getId());
        if (cacheHand == null || cacheHand != usedHand) {
            Integer id = selectedMap.remove(player.getId());
            if (id != null) {
                Entity e = level.getEntity(id);
                if (e != null && e.isAlive() && e instanceof LivingEntity entity)
                    entity.setDeltaMovement(getShift(player, usedHand, entity));
            }
        }
    }

    protected Vec3 getShift(Player player, InteractionHand usedHand, LivingEntity entity) {
        Vec3 view = player.getViewVector(0);

        float yRot = player.getYRot();
        double x = 0 - Math.sin(yRot * Math.PI / 180f);
        double z = Math.cos(yRot * Math.PI / 180f);
        int hand = usedHand == InteractionHand.MAIN_HAND ? 1 : -1;
        Vec3 playerPos = new Vec3(
                player.getX() - (z / 2.5d * hand),
                player.getEyeY() - 1d,
                player.getZ() + (x / 2.5d * hand)
        );
        return view.scale(config.HOLD_DIST.get()).add(playerPos).subtract(entity.position());
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("When activated:").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(new TextComponent("  - Picks up the currently looked at mob from up to ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.pluralize(config.GRAB_DISTANCE.get(), "block") + " away").withStyle(ColorUtil.Tooltip.timeAndDistanceColor))
                .append(" and moves them where the player is looking from a distance of ")
                .append(new TextComponent(StringUtil.pluralize(config.HOLD_DIST.get(), "block") + " away").withStyle(ColorUtil.Tooltip.timeAndDistanceColor)));
        pTooltipComponents.add(new TextComponent("  - If a mob is already being held, additionally cast Telekinetic Defensive spells instead squeeze that mob for ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.formatDecimal(config.DAMAGE.get()) + " damage").withStyle(ColorUtil.Tooltip.damageColor)));
        if (config.DO_DAMAGE.get())
            pTooltipComponents.add(new TextComponent("  - Slamming the mob into walls will do ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(new TextComponent(config.DAMAGE.get() + " damage").withStyle(ColorUtil.Tooltip.timeAndDistanceColor))
                    .append(" per unit of speed upon impact"));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get(), " per tick per unit of movement performed by the mob"));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get() * 10, " per squeeze"));
    }

    // Possibly move up to ActiveSpellItem
    @SubscribeEvent
    public static void onSpellRemoved(MagicSelectedEvent.Remove event) {
        if (event.spell.getItem() instanceof TelekineticDefensiveSpell spell) {
            spell.releaseUsing(event.spell, event.spellSlot.usedHand, event.player.level, event.player);
        }
    }

    public static class TelekineticDefensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.DoubleValue MANA_COST;
        public ForgeConfigSpec.DoubleValue HOLD_DIST;
        public ForgeConfigSpec.DoubleValue GRAB_DISTANCE;
        public ForgeConfigSpec.DoubleValue DAMAGE;
        public ForgeConfigSpec.BooleanValue DO_DAMAGE;

        public final ArmorTier LEVEL;
        private final double manaCost;
        private final double holdDist;
        private final double grabDist;
        private final double damage;
        private final boolean doDamage;

        public TelekineticDefensiveSpellConfig(double manaCost, double holdDist, double grabDist, double damage, boolean doDamage, ArmorTier level) {
            this.manaCost = manaCost;
            this.holdDist = holdDist;
            this.grabDist = grabDist;
            this.damage = damage;
            this.doDamage = doDamage;

            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Defensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost: calculated per tick based on movement of held entity. This is what percentage of that distance becomes mana cost").defineInRange("mana_cost", manaCost, 0, 50);
            HOLD_DIST = builder.comment("How far away the entity is held.").defineInRange("hold_distance", holdDist, 1, 10);
            GRAB_DISTANCE = builder.comment("How far away can the player grab from.").defineInRange("grab_distance", grabDist, 0, 20);
            DAMAGE = builder.comment("How much damage the player should deal per squeeze attack").defineInRange("damage", damage, 0, 200);
            DO_DAMAGE = builder.comment("WARNING: kinetic damage is unstable and unreliable; roughly calculates impacts where the entity has resulting speed of at least 100 units and scales this by the damage.").define("do_damage", doDamage);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }

}
