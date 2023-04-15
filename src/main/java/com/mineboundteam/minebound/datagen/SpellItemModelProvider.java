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
         * If you modify this code, execute the gradle runData task
         */

        /* Fire */

        /* Telekinetic */
        generateModels(ItemRegistry.TELEKINETIC_OFFENSIVE_1.get());
        generateModels(ItemRegistry.TELEKINETIC_UTILITY_2.get());
        generateModels(ItemRegistry.TELEKINETIC_UTILITY_3.get());
        generateModels(ItemRegistry.TELEKINETIC_UTILITY_4.get());

        /* Shield */
        generateModels(ItemRegistry.SHIELD_OFFENSIVE_1.get());
        generateModels(ItemRegistry.SHIELD_OFFENSIVE_2.get());
        generateModels(ItemRegistry.SHIELD_OFFENSIVE_3.get());
        generateModels(ItemRegistry.SHIELD_UTILITY_2.get());
        generateModels(ItemRegistry.SHIELD_UTILITY_3.get());
        generateModels(ItemRegistry.SHIELD_UTILITY_4.get());

        /* Earth */
        generateModels(ItemRegistry.EARTH_DEFENSIVE_1.get());
        generateModels(ItemRegistry.EARTH_DEFENSIVE_2.get());
        generateModels(ItemRegistry.EARTH_DEFENSIVE_3.get());
        generateModels(ItemRegistry.EARTH_DEFENSIVE_4.get());

        /* Ender */
        generateModels(ItemRegistry.ENDER_OFFENSIVE_3.get());

        /* Electric */
        generateModels(ItemRegistry.ELECTRIC_UTILITY_2.get());
        generateModels(ItemRegistry.ELECTRIC_UTILITY_3.get());
        generateModels(ItemRegistry.ELECTRIC_UTILITY_4.get());

        /* Light */

        /* Necrotic */
    }

    private void generateModels(SpellItem item) {
        generateSpellModel(item);
        generateItemModel(item);
    }

    private void generateItemModel(SpellItem item) {
        try {
            withExistingParent(item.getRegistryName().getPath(), new ResourceLocation("item/generated"))
                    .texture("layer0", new ResourceLocation(MineBound.MOD_ID, "magic/spell_item" + (item.level.getValue() + 1)))
                    .texture("layer1", new ResourceLocation(MineBound.MOD_ID, "magic/" + item.magicType.getName() + "/item"))
                    .texture("layer2", new ResourceLocation(MineBound.MOD_ID, "magic/spell_" + item.spellType.getName()));
        } catch (IllegalArgumentException e) {
            // This error will be thrown if a texture needed to generate the model json does not exist yet
        }
    }

    private void generateSpellModel(SpellItem item) {
        try {
            withExistingParent("magic/" + item.getRegistryName().getPath(), new ResourceLocation("item/generated"))
                    .texture("layer0", new ResourceLocation(MineBound.MOD_ID, "magic/" + item.magicType.getName() + "/" + item.spellType.getName()))
                    .texture("layer1", new ResourceLocation(MineBound.MOD_ID, "magic/level" + (item.level.getValue() + 1)));
        } catch (IllegalArgumentException e) {
            // This error will be thrown if a texture needed to generate the model json does not exist yet
        }
    }
}