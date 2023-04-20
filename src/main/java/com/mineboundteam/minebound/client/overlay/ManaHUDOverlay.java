package com.mineboundteam.minebound.client.overlay;

import java.util.List;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellItem;
import com.mineboundteam.minebound.magic.UtilitySpells.ShieldUtilitySpell;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;


public class ManaHUDOverlay extends GuiComponent implements IIngameOverlay {
    private final Minecraft minecraft;
    private static final ResourceLocation manaBar = new ResourceLocation(MineBound.MOD_ID, "textures/gui/mana_bar.png");
    private static final ResourceLocation armorBar = new ResourceLocation(MineBound.MOD_ID, "textures/gui/armor_bar.png");

    public ManaHUDOverlay(){
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if (minecraft.player == null) return;
        
        renderPlayerMana(poseStack);
        renderPlayerShield(poseStack);
    }

    protected void renderPlayerMana(PoseStack matrixStack){
        if (!(minecraft.player.getMainHandItem().getItem() instanceof SpellItem ||
                minecraft.player.getOffhandItem().getItem() instanceof SpellItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof MyrialArmorItem))
            return;

        PlayerManaProvider.PlayerMana mana = minecraft.player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
        if (mana == null || mana.getAvailableManaCap() == 0) return;

        int yOffset = minecraft.getWindow().getGuiScaledHeight() - 66;
        int manaHeight = (int) (52*(mana.getMana() / ((double) mana.getAvailableManaCap())));
        int reductionHeight = (int) (52 * ((mana.getTotalManaCap() - mana.getAvailableManaCap()) / (double) mana.getTotalManaCap())); 
        
        String manaText = Integer.toString(mana.getMana());

        RenderSystem.setShaderTexture(0,manaBar);
        blit(matrixStack, 12, yOffset + (54 - manaHeight), 15, 0, 13, manaHeight, 64, 64);
        blit(matrixStack, 12, yOffset + 2, 30, 0, 13, reductionHeight, 64, 64);
        blit(matrixStack, 10, yOffset, 0, 0, 17, 56, 64, 64);

        int xText = 19 - minecraft.font.width(manaText)/2;
        minecraft.font.draw(matrixStack, manaText, xText + 1, yOffset-10, 0);
        minecraft.font.draw(matrixStack, manaText, xText - 1, yOffset-10, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-9, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-11, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-10, MineBound.MANA_COLOR);
    }

    protected void renderPlayerShield(PoseStack matrixStack){
        List<ShieldUtilitySpell> spells; 
        if(!(spells = PassiveSpellItem.getEquippedSpellItemsOfType(ShieldUtilitySpell.class, minecraft.player)).isEmpty()){            

        }
    }


    
}
