package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.client.renderer.armor.MyrialArmorRenderer;
import com.mineboundteam.minebound.client.renderer.entity.MyriCorpseRenderer;
import com.mineboundteam.minebound.client.screens.AlloyFurnaceScreen;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;


@OnlyIn(Dist.CLIENT)
public class ClientRegistry {

    public static void register(){
        registerKeys();
        registerRenderers();
        registerScreens();
    }


    public static final String MINEBOUND_CATEGORY = "key.categories.minebound";

    public static final KeyMapping PRIMARY_MAGIC = new KeyMapping(getName("primary_magic"), KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE, 4, MINEBOUND_CATEGORY);
    public static final KeyMapping SECONDARY_MAGIC = new KeyMapping(getName("secondary_magic"),
            KeyConflictContext.IN_GAME, InputConstants.Type.MOUSE, 3, MINEBOUND_CATEGORY);
    public static final KeyMapping PRIMARY_MAGIC_SELECT = new KeyMapping(getName("primary_magic_select"),
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_X, MINEBOUND_CATEGORY);
    public static final KeyMapping SECONDARY_MAGIC_SELECT = new KeyMapping(getName("secondary_magic_select"),
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_C, MINEBOUND_CATEGORY);

    public static void registerKeys() {
        net.minecraftforge.client.ClientRegistry.registerKeyBinding(PRIMARY_MAGIC);
        net.minecraftforge.client.ClientRegistry.registerKeyBinding(SECONDARY_MAGIC);
        net.minecraftforge.client.ClientRegistry.registerKeyBinding(PRIMARY_MAGIC_SELECT);
        net.minecraftforge.client.ClientRegistry.registerKeyBinding(SECONDARY_MAGIC_SELECT);
    }

    public static void registerRenderers(){
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.MYRIAL_GLASS.get(), RenderType.translucent());

        EntityRenderers.register(EntityRegistry.MYRI_CORPSE.get(), MyriCorpseRenderer::new);

        GeoArmorRenderer.registerArmorRenderer(MyrialArmorItem.class, () -> new MyrialArmorRenderer());
    }

    public static void registerScreens(){
        MenuScreens.register(MenuRegistry.ALLOY_FURNACE_CONTAINER.get(), AlloyFurnaceScreen::new);
    }

    private static String getName(String name) {
        return "key." + MineBound.MOD_ID + "." + name;
    }
}
