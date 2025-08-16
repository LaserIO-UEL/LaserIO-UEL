package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class LaserIOItemModels extends ItemModelProvider {
    public LaserIOItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LaserIO.MODID, existingFileHelper);
    }

    private void blockItemModel(RegistryObject<Item> blockItemRegistry, String texture) {
        withExistingParent(blockItemRegistry.getId().getPath(), modLoc(texture));
    }

    private void itemModel(RegistryObject<Item> itemRegistry, String parent, String texture) {
        singleTexture(itemRegistry.getId().getPath(), mcLoc(parent), "layer0", modLoc(texture));
    }

    private void itemModel(RegistryObject<Item> itemRegistry, String texture) {
        itemModel(itemRegistry, "item/generated", texture);
    }

    @Override
    protected void registerModels() {
        //Block item models
        blockItemModel(Registration.LASER_NODE_ITEM, "block/laser_node");
        blockItemModel(Registration.LASER_CONNECTOR_ITEM, "block/laser_connector");
        blockItemModel(Registration.LASER_CONNECTOR_ADV_ITEM, "block/laser_connector_advanced");

        //Item models
        itemModel(Registration.LASER_WRENCH, "item/handheld", "item/laser_wrench");
        itemModel(Registration.CARD_HOLDER, "item/card_holder");
        itemModel(Registration.CARD_CLONER, "item/card_cloner");
        itemModel(Registration.FILTER_BASIC, "item/filter_basic");
        itemModel(Registration.FILTER_COUNT, "item/filter_count");
        itemModel(Registration.FILTER_TAG, "item/filter_tag");
        itemModel(Registration.FILTER_MOD, "item/filter_mod");
        itemModel(Registration.FILTER_NBT, "item/filter_nbt");
        itemModel(Registration.LOGIC_CHIP_RAW, "item/logic_chip_raw");
        itemModel(Registration.LOGIC_CHIP, "item/logic_chip");
        itemModel(Registration.OVERCLOCKER_NODE, "item/overclocker_node");
        itemModel(Registration.LOGISTIC_OVERCLOCKER_CARD, "item/logistic_overclocker_card");
        itemModel(Registration.GUIDEBOOK, "item/guidebook");
    }
}