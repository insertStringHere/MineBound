package com.mineboundteam.minebound.client.overlay;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider.PlayerMana;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellItem;
import com.mineboundteam.minebound.magic.UtilitySpells.ShieldUtilitySpell;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.registries.ForgeRegistries;


public class ManaHUDOverlay extends GuiComponent implements IIngameOverlay {
    private static final int TEX_HEIGHT = 128;
    private static final int TEX_WIDTH = 128;
    private final Minecraft minecraft;
    private static final ResourceLocation overlay = new ResourceLocation(MineBound.MOD_ID, "textures/gui/overlay.png");

    private int xOffset;
    private int yOffset;

    public ManaHUDOverlay(){
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if (minecraft.player == null) return;
        
        RenderSystem.setShaderTexture(0,overlay);
        PlayerManaProvider.PlayerMana mana = minecraft.player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
        ItemStack armorSpell = PassiveSpellItem.getHighestEquippedSpellOfType(ShieldUtilitySpell.class, minecraft.player);

        PlayerSelectedSpellsProvider.SelectedSpell primary = minecraft.player.getCapability(PlayerSelectedSpellsProvider.PRIMARY_SPELL).orElse(null);
        PlayerSelectedSpellsProvider.SelectedSpell secondary = minecraft.player.getCapability(PlayerSelectedSpellsProvider.SECONDARY_SPELL).orElse(null);

        this.xOffset = 10;
        this.yOffset = minecraft.getWindow().getGuiScaledHeight() - 10;

        renderPlayerMana(poseStack, mana);
        renderPlayerShield(poseStack, armorSpell);

        renderPlayerSpell(poseStack, primary, 43, 0);
        renderPlayerSpell(poseStack, secondary, 43, 22);

        this.yOffset = minecraft.getWindow().getGuiScaledHeight() - 10;
        renderText(poseStack, mana);
    }



    protected void renderPlayerMana(PoseStack matrixStack, PlayerMana mana){
        if (!(minecraft.player.getMainHandItem().getItem() instanceof SpellItem ||
                minecraft.player.getOffhandItem().getItem() instanceof SpellItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof MyrialArmorItem ||
                minecraft.player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof MyrialArmorItem))
            return;

        int yOffset = this.yOffset - 56;
        int reductionHeight = (int) (52 * ((mana.getTotalManaCap() - mana.getAvailableManaCap()) / (double) mana.getTotalManaCap())); 
        int manaHeight = (int) Math.ceil(52*(mana.getMana() / ((double) mana.getTotalManaCap())));

        blit(matrixStack, xOffset + 2, yOffset + (54 - manaHeight), 15, 0, 13, manaHeight, TEX_WIDTH, TEX_HEIGHT);
        blit(matrixStack, xOffset + 2, yOffset + 2, 30, 0, 13, reductionHeight, TEX_WIDTH, TEX_HEIGHT);
        blit(matrixStack, xOffset, yOffset, 0, 0, 17, 56, TEX_WIDTH, TEX_HEIGHT);

        xOffset += 22;
    }

    protected void renderPlayerShield(PoseStack matrixStack, ItemStack spell){
        if(spell != null && !spell.isEmpty() 
            && spell.getItem() instanceof ShieldUtilitySpell spellItem){       

            CompoundTag shieldTag = spell.getOrCreateTag().getCompound(ShieldUtilitySpell.SHIELD_TAG);
            float hits_remaining = shieldTag.getFloat(ShieldUtilitySpell.HITS_TAG);

            int yOffset = this.yOffset - 66;
            int shieldHeight = (int) Math.ceil(60 * (hits_remaining / shieldTag.getInt(ShieldUtilitySpell.MAX_TAG)));
            int cooldownHeight = (int) (63 * (shieldTag.getInt(ShieldUtilitySpell.COOLDOWN_TAG) / (double) spellItem.config.RECOV_TICKS.get()));
            cooldownHeight = Math.max(cooldownHeight, 0);

            blit(matrixStack, xOffset + 1, yOffset + 2, 22, 56, 20, 63 - cooldownHeight, TEX_WIDTH, TEX_HEIGHT);
            blit(matrixStack, xOffset + 4, yOffset + (60 - shieldHeight) + 2, 42, 56, 14, shieldHeight, TEX_WIDTH, TEX_HEIGHT);
            blit(matrixStack, xOffset, yOffset, 0, 56, 22, 66, TEX_WIDTH, TEX_HEIGHT);

            xOffset += 25;
        }
    }

    protected void renderPlayerSpell(PoseStack poseStack, SelectedSpell spell, int uOffset, int vOffset) {
        if(spell == null || spell.isEmpty() || !(minecraft.player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof MyrialArmorItem)) return;

        ItemStack armor = minecraft.player.getItemBySlot(spell.equippedSlot);
        ItemStack realSpell = ItemStack.of(ArmorNBTHelper.getSpellTag(armor, ArmorNBTHelper.ACTIVE_SPELL).getCompound(spell.index));
        ItemStack item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MineBound.MOD_ID,  "magic/" + realSpell.getItem().getRegistryName().getPath())).getDefaultInstance();


        if(item == null || item.isEmpty()) return;

        int yOffset = this.yOffset - 22;
        RenderSystem.setShaderTexture(0,overlay);
        blit(poseStack, xOffset, yOffset, uOffset, vOffset, 22, 22, TEX_WIDTH, TEX_HEIGHT);
        ItemRenderer renderer = minecraft.getItemRenderer();
        renderer.blitOffset = -100;
        renderer.renderGuiItem(item, xOffset+3, yOffset+3);

        this.yOffset -= 25;
    }

    protected void renderText(PoseStack matrixStack, PlayerMana mana){
        if (mana == null || mana.getAvailableManaCap() == 0) return;

        String manaText = Integer.toString(mana.getMana());
        int yOffset = this.yOffset - 56;
        int xText = 19 - minecraft.font.width(manaText)/2;
        minecraft.font.draw(matrixStack, manaText, xText + 1, yOffset-10, 0);
        minecraft.font.draw(matrixStack, manaText, xText - 1, yOffset-10, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-9, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-11, 0);
        minecraft.font.draw(matrixStack, manaText, xText, yOffset-10, MineBound.MANA_COLOR);
    }


    
}
