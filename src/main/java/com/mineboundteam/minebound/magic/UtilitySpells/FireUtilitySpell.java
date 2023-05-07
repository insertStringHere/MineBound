package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.client.registry.ClientRegistry;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.ListUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class FireUtilitySpell extends PassiveSpellItem {
    private final int manaCost;
    private final int manaReduction;
    private final int aoeRange;
    private final double damage;
    private final int damageRate;


    public FireUtilitySpell(Properties properties, FireUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.FIRE, SpellType.UTILITY);

        this.manaCost = config.MANA_COST.get();
        this.manaReduction = config.MANA_REDUCTION.get();
        this.aoeRange = config.AOE_RANGE.get();
        this.damage = config.DAMAGE.get();
        this.damageRate = config.DAMAGE_RATE.get();
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            List<FireUtilitySpell> equippedSpells = getEquippedSpellItemsOfType(FireUtilitySpell.class, player);
            if (equippedSpells.size() > 0) {
                FireUtilitySpell highestSpell = getHighestSpellItem(equippedSpells);
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("fire_utility", -highestSpell.manaReduction));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 0, false, false));
                player.clearFire();

                player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(utilityToggle -> {
                    if (utilityToggle.fire) {
                        int damageRate = highestSpell.damageRate;
                        Level level = player.getLevel();
                        if (level.getGameTime() % damageRate == 0) {
                            level.playSound(null, player, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.1f, 0.75f);

                            int manaCost = 0;
                            int range = 0;
                            int damage = 0;
                            for (FireUtilitySpell spell : equippedSpells) {
                                manaCost += spell.manaCost;
                                range += spell.aoeRange;
                                damage += spell.damage;
                            }

                            int particleCount = range / 5;
                            for (int degree = 0; degree < 360; degree++) {
                                for (int count = 0; count < particleCount; count++) {
                                    /**
                                     * Derived from {@link LivingEntity#hasLineOfSight(Entity)}
                                     */
                                    Vec3 particleVec = new Vec3(
                                            player.getX() + level.getRandom().nextDouble(range) * Math.cos(degree),
                                            player.getY() + (level.getRandom().nextDouble(range) * ListUtil.randomlyChooseFrom(-1, 1)),
                                            player.getZ() + level.getRandom().nextDouble(range) * Math.sin(degree)
                                    );
                                    if (level.clip(new ClipContext(player.getEyePosition(), particleVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS) {
                                        level.addParticle(ParticleTypes.FLAME,
                                                particleVec.x(),
                                                particleVec.y(),
                                                particleVec.z(),
                                                0, 0, 0);
                                    }
                                }
                            }

                            if (event.side.isServer()) {
                                List<Entity> entities = level.getEntities(player, player.getBoundingBox().inflate(range));
                                for (Entity entity : entities) {
                                    if (entity instanceof LivingEntity && player.hasLineOfSight(entity) && player.distanceTo(entity) <= range) {
                                        entity.hurt(DamageSource.playerAttack(player).setIsFire(), damage);
                                        // + 1 is so the fire doesn't flicker
                                        entity.setRemainingFireTicks(damageRate + 1);
                                        reduceMana(manaCost, player);
                                    }
                                }
                            }
                        }
                    }
                });
            } else {
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("fire_utility", 0));
            }
        }
    }

    @Override
    @SuppressWarnings("resource")
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("  - Gives ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("fire resistance").withStyle(ColorUtil.Tooltip.effectColor(MobEffects.FIRE_RESISTANCE))));
        pTooltipComponents.add(TooltipUtil.enabledHeader);
        pTooltipComponents.add(new TextComponent("    - To all entities within ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.pluralize(aoeRange, "block")).withStyle(ColorUtil.Tooltip.timeAndDistanceColor))
                .append(", deals ")
                .append(new TextComponent(StringUtil.pluralize(damage / 2d, "heart") + " of fire damage").withStyle(ColorUtil.Tooltip.damageColor))
                .append(" every ")
                .append(new TextComponent(StringUtil.pluralize(damageRate / 20d, "second")).withStyle(ColorUtil.Tooltip.timeAndDistanceColor)));
        pTooltipComponents.add(new TextComponent("Multiple copies stack to increase the ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("range").withStyle(ColorUtil.Tooltip.timeAndDistanceColor))
                .append(", ")
                .append(new TextComponent("damage").withStyle(ColorUtil.Tooltip.damageColor))
                .append(", and ")
                .append(new TextComponent("Mana cost").withStyle(ColorUtil.Tooltip.manaColorStyle)));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(manaCost + " Mana").withStyle(ColorUtil.Tooltip.manaColorStyle)).append(" per entity damaged"));
        pTooltipComponents.add(new TextComponent("Reduces ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("Manapool").withStyle(ColorUtil.Tooltip.manaColorStyle))
                .append(" by ")
                .append(new TextComponent(String.valueOf(manaReduction)).withStyle(ColorUtil.Tooltip.reductionColorStyle)));

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Optional<PlayerUtilityToggleProvider.UtilityToggle> toggle = player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).resolve();
            TooltipUtil.appendToggleTooltip(pTooltipComponents, ClientRegistry.FIRE_UTILITY_SPELL_TOGGLE, toggle.isPresent() && toggle.get().fire);
        }
    }

    public static class FireUtilitySpellConfig implements IConfig {
        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue MANA_REDUCTION;
        public ForgeConfigSpec.IntValue AOE_RANGE;
        public ForgeConfigSpec.DoubleValue DAMAGE;
        public ForgeConfigSpec.IntValue DAMAGE_RATE;
        public final ArmorTier LEVEL;

        private final int manaCost;
        private final int manaReduction;
        private final int aoeRange;
        private final double damage;
        private final int damageRate;

        public FireUtilitySpellConfig(int manaCost, int manaReduction, int aoeRange, double damage, int damageRate, ArmorTier level) {
            this.manaCost = manaCost;
            this.manaReduction = manaReduction;
            this.aoeRange = aoeRange;
            this.damage = damage;
            this.damageRate = damageRate;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost per application of damage").defineInRange("mana_cost", manaCost, 0, 10000);
            MANA_REDUCTION = builder.comment("How much total mana will be reduced by").defineInRange("mana_reduction", manaReduction, 0, 10000);
            AOE_RANGE = builder.comment("Area of effect in blocks").defineInRange("aoe_range", aoeRange, 0, 10000);
            DAMAGE = builder.comment("Damage dealt in hearts").defineInRange("damage", damage, 0, 10000);
            DAMAGE_RATE = builder.comment("How often in ticks the spell will damage entities in range (20 ticks = 1 second)").defineInRange("damage_rate", damageRate, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
