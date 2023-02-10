package com.mineboundteam.minebound.client.screens;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.inventory.ArmorForgeMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ArmorForgeScreen extends AbstractContainerScreen<ArmorForgeMenu> {
    public ArmorForgeScreen(ArmorForgeMenu alloyFurnaceContainer, Inventory inventory, Component component) {
        super(alloyFurnaceContainer, inventory, component);
        this.imageWidth = 274;
        this.imageHeight = 206;
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelX += 24;
        this.inventoryLabelY = 109;
        this.titleLabelX += 24;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        if(this.menu.activeSpells.isActive())
            this.font.draw(pPoseStack, new TranslatableComponent("container." + MineBound.MOD_ID + ".active_spells"), 203f, 14f, 0xFBFBFB);
        if(this.menu.passiveSpells.isActive())
            this.font.draw(pPoseStack, new TranslatableComponent("container." + MineBound.MOD_ID + ".passive_spells"), 203f, 90f, 0xFBFBFB);
     }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, new ResourceLocation(MineBound.MOD_ID, "textures/screen/armor_forge.png"));
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, 256, this.imageHeight);
        this.blit(poseStack, this.leftPos + 256, this.topPos + 9, 0, this.imageHeight, 18, 10);
        this.blit(poseStack, this.leftPos + 256, this.topPos + 19, 18, this.imageHeight, 18, 50);
        this.blit(poseStack, this.leftPos + 256, this.topPos + 69, 18, this.imageHeight, 18, 50);
        this.blit(poseStack, this.leftPos + 256, this.topPos + 119, 36, this.imageHeight, 18, 50);

        if (this.menu.activeSpells.isActive())
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (this.menu.slots.get(47 + (i * 3 + j)).isActive())
                        this.blit(poseStack, this.leftPos + j * 21 + 207, this.topPos + i * 21 + 25, 7, 21, 18, 18);

        if (this.menu.passiveSpells.isActive())
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (this.menu.slots.get(56 + (i * 3 + j)).isActive())
                        this.blit(poseStack, this.leftPos + j * 21 + 207, this.topPos + i * 21 + 102, 7, 21, 18, 18);
    }
}
