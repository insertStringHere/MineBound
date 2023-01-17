package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class KeyRegistry {
    public static final String MINEBOUND_CATEGORY = "key.categories.minebound";

    public static final KeyMapping PRIMARY_MAGIC = new KeyMapping(getName("primary_magic"), KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE, 4, MINEBOUND_CATEGORY);
    public static final KeyMapping SECONDARY_MAGIC = new KeyMapping(getName("secondary_magic"),
            KeyConflictContext.IN_GAME, InputConstants.Type.MOUSE, 3, MINEBOUND_CATEGORY);
    public static final KeyMapping PRIMARY_MAGIC_SELECT = new KeyMapping(getName("primary_magic_select"),
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_X, MINEBOUND_CATEGORY);
    public static final KeyMapping SECONDARY_MAGIC_SELECT = new KeyMapping(getName("secondary_magic_select"),
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_C, MINEBOUND_CATEGORY);

    public static void register() {
        ClientRegistry.registerKeyBinding(PRIMARY_MAGIC);
        ClientRegistry.registerKeyBinding(SECONDARY_MAGIC);
        ClientRegistry.registerKeyBinding(PRIMARY_MAGIC_SELECT);
        ClientRegistry.registerKeyBinding(SECONDARY_MAGIC_SELECT);
    }

    private static String getName(String name) {
        return "key." + MineBound.MOD_ID + "." + name;
    }

    @SubscribeEvent
    // TODO: Remove after testing
    public static void testMethod(InputEvent e) {
        if (PRIMARY_MAGIC.consumeClick())
            System.out.println("Primary Magic Pressed");
        if (SECONDARY_MAGIC.consumeClick())
            System.out.println("Secondary Magic Pressed");
        if (PRIMARY_MAGIC_SELECT.consumeClick())
            System.out.println("Primary Magic Select Pressed");
        if (SECONDARY_MAGIC_SELECT.consumeClick())
            System.out.println("Secondary Magic Select Pressed");
    }
}
