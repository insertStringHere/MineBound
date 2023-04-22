package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class FireUtilitySpell extends PassiveSpellItem {
    private final int manaCost;
    private final int aoeRange;
    private final double damage;
    private final int damageRate;


    public FireUtilitySpell(Properties properties, FireUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.FIRE, SpellType.UTILITY);

        this.manaCost = config.MANA_COST.get();
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
                player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(utilityToggle -> {
                    if (utilityToggle.fire) {
                        FireUtilitySpell highestSpell = getHighestSpellItem(equippedSpells);
                        int damageRate = highestSpell.damageRate;
                        Level level = player.getLevel();
                        if (level.getGameTime() % damageRate == 0) {
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
                                    level.addParticle(ParticleTypes.FLAME,
                                            player.getX() + level.getRandom().nextDouble(range) * Math.cos(degree),
                                            player.getY() + (level.getRandom().nextDouble(range) * MineBound.randomlyChooseFrom(-1, 1)),
                                            player.getZ() + level.getRandom().nextDouble(range) * Math.sin(degree),
                                            0, 0, 0);
                                }
                            }

                            if (event.side.isServer()) {
                                List<Entity> entities = level.getEntities(player, new AABB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range));
                                for (Entity entity : entities) {
                                    if (entity instanceof LivingEntity && player.hasLineOfSight(entity) && player.distanceTo(entity) <= range) {
                                        entity.hurt(DamageSource.ON_FIRE, damage);
                                        // + 1 is so the fire doesn't flicker
                                        entity.setRemainingFireTicks(damageRate + 1);
                                        reduceMana(manaCost, player);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void interceptFireDamage(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof Player player && !player.getLevel().isClientSide() && event.getSource().isFire()) {
            List<FireUtilitySpell> equippedSpells = getEquippedSpellItemsOfType(FireUtilitySpell.class, player);
            if (equippedSpells.size() > 0) {
                event.setCanceled(true);
                player.clearFire();
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While equipped in a ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent("utility slot").withStyle(ChatFormatting.DARK_PURPLE))
                .append(":"));
        double rateInSeconds = damageRate / 20d;
        pTooltipComponents.add(new TextComponent("  - To all entities in an ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent("AOE of " + aoeRange + " blocks").withStyle(ChatFormatting.DARK_GREEN))
                .append(", deals ")
                .append(new TextComponent(new DecimalFormat("0.#").format(damage) + " heart" + (damage != 1 ? "s" : "") + " of fire damage").withStyle(ChatFormatting.RED))
                .append(" every ")
                .append(new TextComponent(new DecimalFormat("0.#").format(rateInSeconds) + " second" + (rateInSeconds > 1 ? "s" : "")).withStyle(ChatFormatting.DARK_GREEN)));
        pTooltipComponents.add(new TextComponent("  - Gives fire immunity").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("Multiple copies stack to increase the ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent("range").withStyle(ChatFormatting.DARK_GREEN))
                .append(", ")
                .append(new TextComponent("damage").withStyle(ChatFormatting.RED))
                .append(", and ")
                .append(new TextComponent("Mana cost").withStyle(manaColorStyle)));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent(manaCost + " Mana").withStyle(manaColorStyle)).append(" per entity damaged"));

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            MutableComponent active = new TextComponent("Fire AOE is currently ").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
            Optional<PlayerUtilityToggleProvider.UtilityToggle> toggle = player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).resolve();
            if (toggle.isPresent() && toggle.get().fire)
                pTooltipComponents.add(active.append(new TextComponent("ON").withStyle(ChatFormatting.GREEN)));
            else
                pTooltipComponents.add(active.append(new TextComponent("OFF").withStyle(ChatFormatting.RED)));
        }
    }

    public static class FireUtilitySpellConfig implements IConfig {
        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue AOE_RANGE;
        public ForgeConfigSpec.DoubleValue DAMAGE;
        public ForgeConfigSpec.IntValue DAMAGE_RATE;
        public final ArmorTier LEVEL;

        private final int manaCost;
        private final int aoeRange;
        private final double damage;
        private final int damageRate;

        public FireUtilitySpellConfig(int manaCost, int aoeRange, double damage, int damageRate, ArmorTier level) {
            this.manaCost = manaCost;
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
