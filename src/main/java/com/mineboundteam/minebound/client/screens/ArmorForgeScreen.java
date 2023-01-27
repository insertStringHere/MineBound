package com.mineboundteam.minebound.client.screens;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.inventory.ArmorForgeMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ArmorForgeScreen extends AbstractContainerScreen<ArmorForgeMenu> {
    public ArmorForgeScreen(ArmorForgeMenu alloyFurnaceContainer, Inventory inventory, Component component) {
        super(alloyFurnaceContainer, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelX = this.imageWidth - this.font.width(this.playerInventoryTitle) - 7;
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
        RenderSystem.setShaderTexture(0, new ResourceLocation(MineBound.MOD_ID, "textures/screen/armor_forge.png"));
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
