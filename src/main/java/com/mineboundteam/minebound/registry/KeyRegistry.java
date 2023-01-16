package com.mineboundteam.minebound.registry;

import org.lwjgl.glfw.GLFW;

import com.mineboundteam.minebound.MineBound;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class KeyRegistry {
    public static final String MINEBOUND_CATEGORY = "key.categories.minebound";

    public static final KeyMapping PRIMARY_MAGIC = new KeyMapping(getName("primary_magic"), InputConstants.Type.MOUSE, 4, MINEBOUND_CATEGORY);

    public static void register(){
        ClientRegistry.registerKeyBinding(PRIMARY_MAGIC);
    }

    private static String getName(String name){
        return "key." + MineBound.MOD_ID + "." + name;
    }

    @SubscribeEvent
    // TODO: Remove after testing
    public static void testMethod(InputEvent e){
        if(!Minecraft.getInstance().isPaused()) {
            int key = -1;

            if(e instanceof KeyInputEvent){
                var k = (KeyInputEvent)e;
                if(k.getAction() == GLFW.GLFW_PRESS)
                    key = k.getKey();
            }
            if(e instanceof MouseInputEvent){
                var m = (MouseInputEvent)e;
                if(m.getAction() == GLFW.GLFW_PRESS)
                    key = m.getButton();
            }

            if(key == KeyRegistry.PRIMARY_MAGIC.getKey().getValue())
                        System.out.println("Primary Magic Pressed");
        }

        

    }
}
