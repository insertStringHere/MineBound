package com.mineboundteam.minebound.container;

import com.mineboundteam.minebound.MineBound;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AlloyFurnaceScreen extends AbstractContainerScreen<AlloyFurnaceContainer> {
    public AlloyFurnaceScreen(AlloyFurnaceContainer alloyFurnaceContainer, Inventory inventory, Component component) {
        super(alloyFurnaceContainer, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, new ResourceLocation(MineBound.MOD_ID,"textures/screen/alloy_furnace.png"));

        int topLeftX = (width - imageWidth) / 2;
        int topLeftY = (height - imageHeight) / 2;
        blit(poseStack, topLeftX, topLeftY, 0, 0, imageWidth, imageHeight);
        if (menu.getProgressPercent() > 0) {
            blit(poseStack, topLeftX + 100, topLeftY + 50, 177, 0, (int) (26 * menu.getProgressPercent()), 8);
        }
    }
}
