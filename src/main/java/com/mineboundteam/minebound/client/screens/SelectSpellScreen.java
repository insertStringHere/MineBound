package com.mineboundteam.minebound.client.screens;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.inventory.SelectSpellMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SelectSpellScreen extends AbstractContainerScreen<SelectSpellMenu> {

    public SelectSpellScreen(SelectSpellMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 348;
        this.imageHeight = 128;
    }
    
    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        // Could be fixed, but I don't know if it's necessary:

        /*if(this.menu.passiveSpells.getContainerSize() > 0)
            this.font.draw(pPoseStack, new TranslatableComponent("container." + MineBound.MOD_ID + ".active_spells"), 114f, 31f, 0xFBFBFB); */
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, new ResourceLocation(MineBound.MOD_ID, "textures/screen/spell_selection.png"));

        if(this.menu.activeSpells.getContainerSize() > 0) {
            this.blit(pPoseStack, this.leftPos+109, this.topPos+26, 109, 26, 130, 73);
            if(this.menu.activeSpells.primarySelected != -1)
                this.blit(pPoseStack, this.leftPos+ 116 + (menu.activeSpells.primarySelected%6*19), this.topPos+35+(menu.activeSpells.primarySelected/6*19), 0, 128, 18, 18);
            if(this.menu.passiveSpells.secondarySelected != -1)
            this.blit(pPoseStack, this.leftPos+ 116 + (menu.activeSpells.secondarySelected%6*19), this.topPos+35+(menu.activeSpells.secondarySelected/6*19), 18, 128, 18, 18);

        }
        if(this.menu.passiveSpells.getContainerSize() > 0)
            this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, 47, 128);
        
    }
    
}
