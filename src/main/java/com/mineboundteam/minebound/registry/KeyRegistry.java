package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public interface KeyRegistry {

    public static final KeyMapping TOGGLE_GLOW = new KeyMapping(getName("toggle_glow"), InputConstants.KEY_V, KeyMapping.CATEGORY_INTERFACE);

    public static void register(){
        ClientRegistry.registerKeyBinding(TOGGLE_GLOW);
    }

    private static String getName(String name){
        return "key." + MineBound.MOD_ID + "." + name;
    }
}
