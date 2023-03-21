package com.mineboundteam.minebound.datagen;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import com.mineboundteam.minebound.magic.SpellItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SpellItemModelProvider extends ItemModelProvider {
    public SpellItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MineBound.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        /*
         * Spell Element
         * [offensive spells]
         * [defensive spells]
         * [utility spells]
         */

        /* Fire */

        /* Telekinetic */
        generateItemModel(ItemRegistry.TELEKINETIC_OFFENSIVE_1.get());
        generateItemModel(ItemRegistry.TELEKINETIC_UTILITY_2.get());
        generateItemModel(ItemRegistry.TELEKINETIC_UTILITY_3.get());
        generateItemModel(ItemRegistry.TELEKINETIC_UTILITY_4.get());

        /* Shield */
        generateItemModel(ItemRegistry.SHIELD_OFFENSIVE_1.get());
        generateItemModel(ItemRegistry.SHIELD_OFFENSIVE_2.get());
        generateItemModel(ItemRegistry.SHIELD_OFFENSIVE_3.get());
        generateItemModel(ItemRegistry.SHIELD_UTILITY_2.get());
        generateItemModel(ItemRegistry.SHIELD_UTILITY_3.get());
        generateItemModel(ItemRegistry.SHIELD_UTILITY_4.get());

        /* Earth */

        /* Ender */

        /* Electric */
        generateItemModel(ItemRegistry.ELECTRIC_UTILITY_2.get());
        generateItemModel(ItemRegistry.ELECTRIC_UTILITY_3.get());
        generateItemModel(ItemRegistry.ELECTRIC_UTILITY_4.get());

        /* Light */

        /* Necrotic */
    }

    private void generateItemModel(SpellItem item) {
        withExistingParent(item.getRegistryName().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(MineBound.MOD_ID, "magic/spell_item" + (item.level.getValue() + 1)))
                .texture("layer1", new ResourceLocation(MineBound.MOD_ID, "magic/" + item.magicType.getName() + "/item"))
                .texture("layer2", new ResourceLocation(MineBound.MOD_ID, "magic/spell_" + item.spellType.getName()));
    }

    // In preparation for Griffin's changes
    private void generateSpellModel(SpellItem item) {
        withExistingParent(item.getRegistryName().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(MineBound.MOD_ID, "magic/" + item.magicType.getName() + "/" + item.spellType.getName()))
                .texture("layer1", new ResourceLocation(MineBound.MOD_ID, "magic/level" + (item.level.getValue() + 1)));
    }
}
