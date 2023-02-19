package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.registry.ClientRegistry;
import com.mineboundteam.minebound.registry.ParticleRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class FireOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private static final HashMap<ArmorTier, RegistryObject<SimpleParticleType>> particles = new HashMap<>() {{
        put(ArmorTier.EFFIGY, ParticleRegistry.FIRE_OFFENSIVE_1);
        put(ArmorTier.SUIT, ParticleRegistry.FIRE_OFFENSIVE_2);
        put(ArmorTier.SYNERGY, ParticleRegistry.FIRE_OFFENSIVE_3);
    }};

    public FireOffensiveSpell(Properties properties, FireOffensiveSpellConfig config) {
        super(properties, config.LEVEL);

        this.manaCost = config.MANA_COST.get();
    }

    // TODO: tie in with keybindings based on where spell is equipped
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient() && event.phase == TickEvent.Phase.START && ClientRegistry.PRIMARY_MAGIC.isDown()) {
            Player player = event.player;
            float yRot = player.getYRot();
            float xRot = player.getXRot();
            double x = (0 - Math.sin(yRot * Math.PI / 180f));
            double y = (0 - Math.sin(xRot * Math.PI / 180f));
            double z = (Math.cos(yRot * Math.PI / 180f));
            for (int i = 0; i < 360; i++) {
                if (i % 10 == 0) {
                    player.getLevel().addParticle(particles.get(ArmorTier.SUIT).get(),
                            player.getX() + x - (z * Math.sin(i) * (Math.random() * 0.075d)),
                            player.getY() + 1.4d + y + (Math.cos(i) * (Math.random() * 0.075d)),
                            player.getZ() + z + (x * Math.sin(i) * (Math.random() * 0.075d)),
                            x * 1000d - (Math.random() * (z * Math.sin(i) * 75d)),
                            y * 1000d + (Math.random() * (Math.cos(i) * 75d)),
                            z * 1000d + (Math.random() * (x * Math.sin(i) * 75d))
                    );

                }
            }

//            if (player.level.getGameTime() % 20 == 0)
//                super.reduceMana(manaCost, player);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static class FireOffensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public ArmorTier LEVEL;
        private final int manaCost;

        public FireOffensiveSpellConfig(int manaCost, ArmorTier level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
