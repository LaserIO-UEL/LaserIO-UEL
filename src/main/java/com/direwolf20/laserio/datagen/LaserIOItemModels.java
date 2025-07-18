package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class LaserIOItemModels extends ItemModelProvider {
    public LaserIOItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LaserIO.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Block item models
        withExistingParent(Registration.LASER_NODE_ITEM.getId().getPath(), modLoc("block/laser_node"));
        withExistingParent(Registration.LASER_CONNECTOR_ITEM.getId().getPath(), modLoc("block/laser_connector"));
        withExistingParent(Registration.LASER_CONNECTOR_ADV_ITEM.getId().getPath(), modLoc("block/laser_connector_advanced"));

        //Item models
        singleTexture(Registration.LASER_WRENCH.getId().getPath(), mcLoc("item/handheld"), "layer0", modLoc("item/laser_wrench"));
        singleTexture(Registration.CARD_HOLDER.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/card_holder"));
        singleTexture(Registration.CARD_CLONER.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/card_cloner"));
        singleTexture(Registration.FILTER_BASIC.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/filter_basic"));
        singleTexture(Registration.FILTER_COUNT.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/filter_count"));
        singleTexture(Registration.FILTER_TAG.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/filter_tag"));
        singleTexture(Registration.FILTER_MOD.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/filter_mod"));
        singleTexture(Registration.FILTER_NBT.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/filter_nbt"));
        singleTexture(Registration.LOGIC_CHIP.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/logic_chip"));
        singleTexture(Registration.LOGIC_CHIP_RAW.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/logic_chip_raw"));
        singleTexture(Registration.OVERCLOCKER_NODE.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/overclocker_node"));
        singleTexture(Registration.LOGISTIC_OVERCLOCKER_CARD.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/logistic_overclocker_card"));
    }
}