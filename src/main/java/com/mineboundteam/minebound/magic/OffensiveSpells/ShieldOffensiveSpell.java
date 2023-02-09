package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.SpellLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ShieldOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private final SpellLevel level;
    private boolean active = false;
    private HashMap<SpellLevel, float[]> spellLevelDmgMult = new HashMap<>();

    public ShieldOffensiveSpell(Properties properties, ShieldOffensiveSpellConfig config) {
        super(properties);

        this.manaCost = config.MANA_COST.get();
        this.level = config.LEVEL;

        // Reduce damage by 50% and reflect 40%
        spellLevelDmgMult.put(SpellLevel.Level1, new float[]{0.5f, 0.4f});
        // Reduce damage by 70% and reflect 60%
        spellLevelDmgMult.put(SpellLevel.Level2, new float[]{0.7f, 0.6f});
        // Reduce damage by 100% and reflect 80%
        spellLevelDmgMult.put(SpellLevel.Level3, new float[]{1.0f, 0.8f});
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        active = !active;
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @SubscribeEvent
    public void triggerSpell(LivingDamageEvent event) {
        if (!event.getEntity().level.isClientSide() && active && event.getEntityLiving() instanceof Player player) {
            Entity sourceEntity = event.getSource().getEntity();
            if (sourceEntity != null) {
                float dmgAmount = event.getAmount();
                event.setAmount(dmgAmount * (1 - spellLevelDmgMult.get(level)[0]));
                sourceEntity.hurt(DamageSource.thorns(player), dmgAmount * spellLevelDmgMult.get(level)[1]);
                super.reduceMana(manaCost, player);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("While pressing the magic keybind, reduces damage taken from mobs by " + (spellLevelDmgMult.get(level)[0] * 100) + "% and reflects " + (spellLevelDmgMult.get(level)[1] * 100) + "% of the initial damage"));
        pTooltipComponents.add(new TextComponent("Costs " + manaCost + " Mana per reflect"));
    }

    public static class ShieldOffensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public final SpellLevel LEVEL;

        private final int manaCost;

        public ShieldOffensiveSpellConfig(int manaCost, SpellLevel level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Shield Offensive");
            MANA_COST = builder.comment("Tier " + LEVEL.getValue() + " Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop();
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
