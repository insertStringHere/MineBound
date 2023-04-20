package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider.PlayerMana;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.PrimarySpellProvider.PrimarySelected;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider.UtilityToggle;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.capabilities.network.CapabilitySync;
import com.mineboundteam.minebound.capabilities.network.CapabilitySync.SelectedSpellsSync;
import com.mineboundteam.minebound.client.registry.ClientRegistry;
import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.config.ManaConfig;
import com.mineboundteam.minebound.config.registry.ArmorConfigRegistry;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.helper.UseSpellHelper;
import com.mineboundteam.minebound.magic.network.MagicAnimationSync;
import com.mineboundteam.minebound.magic.network.MagicButtonSync;
import com.mineboundteam.minebound.magic.network.MagicSync;
import com.mineboundteam.minebound.magic.network.MagicButtonSync.ButtonMsg.MsgType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class MagicEvents {
    protected static final EnumSet<EquipmentSlot> armorSlots = EnumSet.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST,
            EquipmentSlot.LEGS, EquipmentSlot.FEET);

    @OnlyIn(Dist.CLIENT)
    protected static int useCountPrimary, useCountSecondary;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.player.level.getGameTime() % 10 == 0
                    && event.phase == TickEvent.Phase.START) {
            handlePlayerArmor(event.player);
            event.player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(handlePlayerMana(event.player));
        }

        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerSelectedSpellsProvider.PRIMARY_SPELL).ifPresent(spell -> {
                if (spell.equippedSlot != null && !event.player.hasItemInSlot(spell.equippedSlot)) {
                    spell.equippedSlot = null;
                    spell.index = 0;
                    CapabilitySync.NET_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.player), new SelectedSpellsSync(true, spell.equippedSlot, spell.index));
                }
            });
            event.player.getCapability(PlayerSelectedSpellsProvider.SECONDARY_SPELL).ifPresent(spell -> {
                if (spell.equippedSlot != null && !event.player.hasItemInSlot(spell.equippedSlot)) {
                    spell.equippedSlot = null;
                    spell.index = 0;
                    CapabilitySync.NET_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.player), new SelectedSpellsSync(false, spell.equippedSlot, spell.index));
                }
            });
        }

        if(event.side == LogicalSide.CLIENT){
            if(ClientRegistry.PRIMARY_MAGIC.isDown()) {
                UseSpellHelper.useSpellTick(event.player, PlayerSelectedSpellsProvider.PRIMARY_SPELL, useCountPrimary++);
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonHeldMsg(MagicButtonSync.ButtonHeldMsg.MsgType.PRIMARY, useCountPrimary));
            }
            if (ClientRegistry.SECONDARY_MAGIC.isDown()) {
                UseSpellHelper.useSpellTick(event.player, PlayerSelectedSpellsProvider.SECONDARY_SPELL, useCountSecondary++);
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonHeldMsg(MagicButtonSync.ButtonHeldMsg.MsgType.SECONDARY, useCountSecondary));
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("resource")
    public static void onButtonPress(InputEvent event) {
        if (ClientRegistry.PRIMARY_MAGIC_SELECT.consumeClick())
            MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.PRIMARY_MENU));
        else if (ClientRegistry.SECONDARY_MAGIC_SELECT.consumeClick())
            MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.SECONDARY_MENU));

        if(ClientRegistry.PRIMARY_MAGIC.consumeClick())
            useCountPrimary = 0;
        if(ClientRegistry.SECONDARY_MAGIC.consumeClick())
            useCountSecondary = 0;

        LocalPlayer player = Minecraft.getInstance().player;
        UtilityToggle toggle = null;
        if(player != null) {
            var cap = player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE);
            if(cap.isPresent())
                toggle = cap.resolve().get();
        }
        
        if (ClientRegistry.FIRE_UTILITY_SPELL_TOGGLE.consumeClick()) {
            MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.FIRE_UTILITY_TOGGLE));
            if(toggle != null)
                toggle.fire = !toggle.fire;
        }
        if (ClientRegistry.EARTH_UTILITY_SPELL_TOGGLE.consumeClick()) {
            MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.EARTH_UTILITY_TOGGLE));
            if(toggle != null)
                toggle.earth = !toggle.earth;
        }
        if (ClientRegistry.LIGHT_UTILITY_SPELL_TOGGLE.consumeClick()) {
            MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.LIGHT_UTILITY_TOGGLE));
            if(toggle != null)
                toggle.light = !toggle.light;
        }
        if (ClientRegistry.ENDER_UTILITY_SPELL_TOGGLE.consumeClick()) {
            MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.ENDER_UTILITY_TOGGLE));
            if(toggle != null)
                toggle.light = !toggle.light;
        }

    }

    // If the magic keybinds are bound to mouse buttons
    @SubscribeEvent
    public static void onButtonPress(InputEvent.MouseInputEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (ClientRegistry.PRIMARY_MAGIC.consumeClick()){
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.PRIMARY_PRESSED));
            }
            if (ClientRegistry.SECONDARY_MAGIC.consumeClick()){
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.SECONDARY_PRESSED));
            }
        } else if (Minecraft.getInstance().getConnection() != null && event.getAction() == GLFW.GLFW_RELEASE) {
            if (event.getButton() == ClientRegistry.PRIMARY_MAGIC.getKey().getValue())
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.PRIMARY_RELEASED));
            if (event.getButton() == ClientRegistry.SECONDARY_MAGIC.getKey().getValue())
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.SECONDARY_RELEASED));
        }
    }

    // If the magic keybinds are bound to keyboard keys
    @SubscribeEvent
    public static void onButtonPress(InputEvent.KeyInputEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            if (ClientRegistry.PRIMARY_MAGIC.consumeClick())
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.PRIMARY_PRESSED));
            if (ClientRegistry.SECONDARY_MAGIC.consumeClick())
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.SECONDARY_PRESSED));
        } else if (Minecraft.getInstance().getConnection() != null && event.getAction() == GLFW.GLFW_RELEASE) {
            if (event.getKey() == ClientRegistry.PRIMARY_MAGIC.getKey().getValue())
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.PRIMARY_RELEASED));
            if (event.getKey() == ClientRegistry.SECONDARY_MAGIC.getKey().getValue())
                MagicSync.NET_CHANNEL.sendToServer(new MagicButtonSync.ButtonMsg(MsgType.SECONDARY_RELEASED));
        }
    }

    protected static UUID healthReductionID = new UUID(237427279, 347509);

    protected static void handlePlayerArmor(Player player) {
        var tier = new Object() {
            ArmorTier max = null;
        };

        for (ItemStack item : player.getArmorSlots()) {
            if (!item.isEmpty() && item.getItem() instanceof MyrialArmorItem armorItem){
                boolean recovering = item.getOrCreateTag().getBoolean("recovery");
                if (armorItem.getDamage(item) >= armorItem.getMaxDamage(item))
                    recovering = true;
                if (1.0d * armorItem.getDamage(item) / armorItem.getMaxDamage(item) <= 0.25d)
                    recovering = false;

                if (tier.max == null || tier.max.getValue() < armorItem.getTier().getValue())
                    tier.max = armorItem.getTier();

                int armorDamage = armorItem.getDamage(item);
                if (armorDamage > 0) {
                    FoodData pFoodData = player.getFoodData();
                    if (pFoodData.getSaturationLevel() > 0 && pFoodData.getFoodLevel() >= 20) {
                        float f = Math.min(pFoodData.getSaturationLevel(), 6.0F);
                        armorItem.setDamage(item, (int) (armorDamage - f / 6));
                        pFoodData.addExhaustion(f / 16.0f);
                    } else if (pFoodData.getFoodLevel() >= 16) {
                        armorItem.setDamage(item, armorDamage - 1);
                        pFoodData.addExhaustion(0.125f);
                    }

                    if (armorItem.getDamage(item) < 0)
                        armorItem.setDamage(item, 0);
                }
                
                item.getOrCreateTag().putBoolean("recovering", recovering);
            }
        }

        int healthReduction = tier.max == null ? 0 : (tier.max.getValue() + 1) * -2;
        AttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);


        if (healthAttribute.getModifier(healthReductionID) != null
                    && (int) healthAttribute.getModifier(healthReductionID).getAmount() != healthReduction) {
            healthAttribute.removeModifier(healthReductionID);
            healthAttribute.addTransientModifier(
                    new AttributeModifier(healthReductionID, "HealthReduce", healthReduction, Operation.ADDITION));
        } else if (healthAttribute.getModifier(healthReductionID) == null) {
            healthAttribute.addTransientModifier(new AttributeModifier(healthReductionID, "HealthReduce", healthReduction, Operation.ADDITION));
        }

        if (player.getHealth() > player.getMaxHealth()) {
            player.hurt(DamageSource.STARVE, player.getHealth() - player.getMaxHealth());
        }
    }

    protected static NonNullConsumer<PlayerMana> handlePlayerMana(Player player) {
        return mana -> {
            int manaBoost = 0;
            int totalMana = mana.getBaseManaCap();
            int recBoost = 0;

            boolean totalArmorSet = true;
            boolean availableArmorSet = true;
            ArmorTier tier = null;
            List<ItemStack> mArmors = new ArrayList<>();

            // check each armor slot and get their bonuses to recovery and cap
            for (EquipmentSlot slot : armorSlots) {
                ItemStack armorStack = player.getItemBySlot(slot);
                Item armorItem = armorStack.getItem();

                if (armorItem instanceof MyrialArmorItem) {
                    ArmorConfig config = ((MyrialArmorItem) armorItem).getConfig();
                    totalMana += config.MANAPOOL.get();

                    if (!armorStack.getOrCreateTag().getBoolean("recovery")) {
                        mArmors.add(player.getItemBySlot(slot));
                        manaBoost += config.MANAPOOL.get();
                        recBoost += config.RECOVERY.get();
                    } else {
                        availableArmorSet = false;
                    }

                    if (tier == null)
                        tier = ((MyrialArmorItem) armorItem).getTier();
                    if (tier == ((MyrialArmorItem) armorItem).getTier())
                        continue;
                }
                availableArmorSet = false;
                totalArmorSet = false;
            }

            if (totalArmorSet) {
                ArmorConfig setConfig = ArmorConfigRegistry.SET_BONUS_MAP.get(tier);
                totalMana += setConfig.MANAPOOL.get();
                if (availableArmorSet) {
                    manaBoost += setConfig.MANAPOOL.get();
                    recBoost += setConfig.RECOVERY.get();
                }
            }

            for (Integer modifier : mana.getManaCapModifiers().values()) {
                totalMana += modifier;
                manaBoost += modifier;
            }

            // if mana is recovered, calculate charge drained from armor durability
            if (mana.getBaseManaCap() + manaBoost > mana.getMana()) {
                for (ItemStack stack : mArmors) {
                    if (player.getRandom().nextInt(3) == 0 && stack.getDamageValue() < stack.getMaxDamage()) {
                        stack.setDamageValue(stack.getDamageValue() + 1);
                    }
                }
            }

            mana.setTotalManaCap(totalMana);
            mana.setAvailableManaCap(mana.getBaseManaCap() + manaBoost);
            mana.addMana(mana.getManaRecRate() + recBoost);

            CapabilitySync.NET_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new CapabilitySync.ManaSync(mana.getMana(), totalMana));
        };
    }

    private static final HashMap<ServerPlayer, CompoundTag> cacheData = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerKilled(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && ManaConfig.keepArmor.get()) {
            CompoundTag armorTag = new CompoundTag();
            for (ItemStack item : player.getArmorSlots()) {
                if (!item.isEmpty() && item.getItem() instanceof MyrialArmorItem) {
                    CompoundTag armorItem = new CompoundTag();
                    item.save(armorItem);
                    armorTag.put(Player.getEquipmentSlotForItem(item).getName(), armorItem);
                    item.shrink(1);
                }
            }

            synchronized (cacheData) {
                cacheData.put(player, armorTag);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (ManaConfig.keepArmor.get() && event.isWasDeath() && event.getOriginal() instanceof ServerPlayer original) {
            CompoundTag armorTag;
            synchronized (cacheData) {
                armorTag = cacheData.remove(original);
            }

            for (String key : armorTag.getAllKeys())
                event.getPlayer().setItemSlot(EquipmentSlot.byName(key), ItemStack.of(armorTag.getCompound(key)));
        }
    }

    public static ConcurrentHashMap<Integer, Integer> playerStates = new ConcurrentHashMap<>(20);
    @SubscribeEvent
    public static void playerArmRenderer(RenderPlayerEvent event){
        Player player = event.getPlayer();
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
        
        for(EquipmentSlot slot : armorSlots){
            if(player.getItemBySlot(slot).getItem() instanceof MyrialArmorItem)
                switch(slot){
                    case CHEST -> {
                        model.jacket.visible = false;
                        model.leftSleeve.visible = false;
                        model.rightSleeve.visible = false;
                    }
                    case HEAD -> {
                        model.hat.visible = false;
                    }
                    case LEGS -> {
                        model.leftPants.visible = false;
                        model.rightPants.visible = false; 
                    }
                    default ->{}
                }
        }

        if(!model.rightArmPose.isTwoHanded() && playerStates.containsKey(event.getPlayer().getId())){
            int arms = playerStates.get(event.getPlayer().getId());
            if((arms & MagicAnimationSync.ArmUsersMsg.PRIMARY) != 0)
                model.rightArmPose = ArmPose.SPYGLASS;
            if((arms & MagicAnimationSync.ArmUsersMsg.SECONDARY) != 0)
                model.leftArmPose = ArmPose.SPYGLASS;
        }
    }

    @SubscribeEvent
    public static void onGameTick(ServerTickEvent event){
        MagicSync.NET_CHANNEL.send(PacketDistributor.ALL.noArg(), new MagicAnimationSync.ArmUsersMsg(playerStates));
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void firstPersonArmRender(RenderArmEvent event){
        // TODO: figure out how to force left hand to render
        switch(event.getArm()){
            case RIGHT -> {
                Optional<PrimarySelected> cap = event.getPlayer().getCapability(PlayerSelectedSpellsProvider.PRIMARY_SPELL).resolve();
                if(ClientRegistry.PRIMARY_MAGIC.isDown() && cap.isPresent() && !cap.get().isEmpty()){
                    event.getPoseStack().translate(0, .3F, 0f);
                    event.getPoseStack().pushPose();
                }
            }
            case LEFT  -> {
                Optional<PrimarySelected> cap = event.getPlayer().getCapability(PlayerSelectedSpellsProvider.PRIMARY_SPELL).resolve();
                if(ClientRegistry.SECONDARY_MAGIC.isDown() && cap.isPresent() && !cap.get().isEmpty()){
                    event.getPoseStack().translate(0, .3F, 0f);
                    event.getPoseStack().pushPose();
                }
            }
        }
    }
}