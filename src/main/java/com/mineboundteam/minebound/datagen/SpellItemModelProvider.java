package com.mineboundteam.minebound.datagen;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import com.mineboundteam.minebound.magic.SpellItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class SpellItemModelProvider extends ItemModelProvider {
    public SpellItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MineBound.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> item : ItemRegistry.ITEMS.getEntries()) {
            if (item.get() instanceof SpellItem spellItem) {
                generateModel(spellItem);
            }
        }
    }

    private void generateModel(SpellItem item) {
        withExistingParent(item.getRegistryName().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(MineBound.MOD_ID, "magic/spell_item" + (item.level.getValue() + 1)))
                .texture("layer1", new ResourceLocation(MineBound.MOD_ID, "magic/" + item.magicType.getName() + "/item"))
                .texture("layer2", new ResourceLocation(MineBound.MOD_ID, "magic/spell_" + item.spellType.getName()));
    }
}
