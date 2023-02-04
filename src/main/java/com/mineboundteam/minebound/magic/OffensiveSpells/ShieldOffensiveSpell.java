package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.SpellLevel;
import com.mineboundteam.minebound.mana.PlayerManaProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ShieldOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private boolean active = false;

    public ShieldOffensiveSpell(Properties properties, SpellLevel level, ShieldOffensiveSpellConfig config) {
        super(properties, level);

        this.manaCost = config.MANA_COST.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        active = !active;
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @SubscribeEvent
    public void triggerSpell(LivingDamageEvent event) {
        if (!event.getEntity().level.isClientSide() && active) {
            if (event.getEntityLiving() instanceof Player player) {
                Entity sourceEntity = event.getSource().getEntity();
                if (sourceEntity != null) {
                    float dmgAmount = event.getAmount();
                    // Reduce by 50%
                    event.setAmount(dmgAmount * 0.5f);
                    // Reflect 40%
                    sourceEntity.hurt(DamageSource.thorns(player), dmgAmount * 0.4f);
                    super.reduceMana(manaCost, player);
                }
            }
        }
    }

    public static class ShieldOffensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;

        private final int manaCost;
        private final SpellLevel level;

        public ShieldOffensiveSpellConfig(int manaCost, SpellLevel level) {
            this.manaCost = manaCost;
            this.level = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Shield Offensive");
            MANA_COST = builder.comment("Tier " + level.getValue() + " Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop();
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
