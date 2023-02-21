package com.mineboundteam.minebound.client.overlay;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.SpellItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MineBound.MOD_ID)
public class ManaHUDOverlay extends GuiComponent {
    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation manaBar = new ResourceLocation(MineBound.MOD_ID, "textures/gui/mana_bar.png");
    private static final ResourceLocation manaFrame = new ResourceLocation(MineBound.MOD_ID, "textures/gui/mana_bar_frame.png");

    @SubscribeEvent
    public static void onPostRender(final RenderGameOverlayEvent.Post event){
        PoseStack matrixStack = event.getMatrixStack();

        if (minecraft.player == null) return;

        if (!(minecraft.player.getMainHandItem().getItem() instanceof SpellItem ||
                minecraft.player.getOffhandItem().getItem() instanceof SpellItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof MyrialArmorItem))
            return;


        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;

        PlayerManaProvider.PlayerMana mana = minecraft.player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
        if (mana == null) return;

        int yOffset = minecraft.getWindow().getGuiScaledHeight() - 42;
        int manaHeight = (int) (27*(mana.getMana() / ((double) mana.getTotalManaCap())) + 3);
        String manaText = Integer.toString(mana.getMana());
        int textBlue = (77 << 16) + (106 << 8) + (255);

        RenderSystem.setShaderTexture(0,manaBar);
        blit(matrixStack, 10, yOffset + (30 - manaHeight), 0, 0, 20, manaHeight, 20, 32);

        RenderSystem.setShaderTexture(0,manaFrame);
        blit(matrixStack, 10, yOffset, 0, 0, 20, 32, 20, 32);

        int xText = 20 - minecraft.font.width(manaText)/2;
        minecraft.font.draw(matrixStack, manaText, xText + 1, yOffset-10, 0);
        minecraft.font.draw(matrixStack, manaText, xText - 1, yOffset-10, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-9, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-11, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-10, textBlue);

    }
}
