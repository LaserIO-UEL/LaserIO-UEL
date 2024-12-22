package com.direwolf20.laserio.setup;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.blockentities.LaserConnectorAdvBE;
import com.direwolf20.laserio.common.blockentities.LaserConnectorBE;
import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.blocks.LaserConnector;
import com.direwolf20.laserio.common.blocks.LaserConnectorAdv;
import com.direwolf20.laserio.common.blocks.LaserNode;
import com.direwolf20.laserio.common.containers.*;
import com.direwolf20.laserio.common.items.*;
import com.direwolf20.laserio.common.items.cards.CardEnergy;
import com.direwolf20.laserio.common.items.cards.CardFluid;
import com.direwolf20.laserio.common.items.cards.CardItem;
import com.direwolf20.laserio.common.items.cards.CardRedstone;
import com.direwolf20.laserio.common.items.filters.*;
import com.direwolf20.laserio.common.items.upgrades.OverclockerCard;
import com.direwolf20.laserio.common.items.upgrades.OverclockerNode;
import com.direwolf20.laserio.datagen.customrecipes.CardClearRecipe;
import com.electronwill.nightconfig.core.Config;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static com.direwolf20.laserio.client.particles.ModParticles.PARTICLE_TYPES;
import static com.direwolf20.laserio.common.Config.ENERGYTIERS_PATH;
import static com.direwolf20.laserio.common.LaserIO.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LaserIO.MODID);
    public static final RegistryObject<CardClearRecipe.Serializer> CARD_CLEAR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("cardclear", CardClearRecipe.Serializer::new);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        CONTAINERS.register(bus);
        PARTICLE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }

    //Some common properties for our blocks and items
    //public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);

    //Blocks
    public static final RegistryObject<Block> LaserConnector = BLOCKS.register("laser_connector", LaserConnector::new);
    public static final RegistryObject<Item> LaserConnector_ITEM = ITEMS.register("laser_connector", () -> new BlockItem(LaserConnector.get(), new Item.Properties()));
    public static final RegistryObject<LaserNode> LaserNode = BLOCKS.register("laser_node", LaserNode::new);
    public static final RegistryObject<Item> LaserNode_ITEM = ITEMS.register("laser_node", () -> new BlockItem(LaserNode.get(), new Item.Properties()));
    public static final RegistryObject<Block> LaserConnectorAdv = BLOCKS.register("laser_connector_advanced", LaserConnectorAdv::new);
    public static final RegistryObject<Item> LaserConnectorAdv_ITEM = ITEMS.register("laser_connector_advanced", () -> new BlockItem(LaserConnectorAdv.get(), new Item.Properties()));

    //BlockEntities (Not TileEntities - Honest)
    public static final RegistryObject<BlockEntityType<LaserNodeBE>> LaserNode_BE = BLOCK_ENTITIES.register("lasernode", () -> BlockEntityType.Builder.of(LaserNodeBE::new, LaserNode.get()).build(null));
    public static final RegistryObject<BlockEntityType<LaserConnectorBE>> LaserConnector_BE = BLOCK_ENTITIES.register("laserconnector", () -> BlockEntityType.Builder.of(LaserConnectorBE::new, LaserConnector.get()).build(null));
    public static final RegistryObject<BlockEntityType<LaserConnectorAdvBE>> LaserConnectorAdv_BE = BLOCK_ENTITIES.register("laserconnectoradv", () -> BlockEntityType.Builder.of(LaserConnectorAdvBE::new, LaserConnectorAdv.get()).build(null));

    //Items
    public static final RegistryObject<Item> Laser_Wrench = ITEMS.register("laser_wrench", LaserWrench::new);
    public static final RegistryObject<Item> Card_Holder = ITEMS.register("card_holder", CardHolder::new);
    public static final RegistryObject<Item> Card_Cloner = ITEMS.register("card_cloner", CardCloner::new);

    //Cards
    public static final RegistryObject<Item> Card_Item = ITEMS.register("card_item", CardItem::new);
    public static final RegistryObject<Item> Card_Fluid = ITEMS.register("card_fluid", CardFluid::new);
    public static final RegistryObject<Item> Card_Energy = ITEMS.register("card_energy", CardEnergy::new);
    public static final RegistryObject<Item> Card_Redstone = ITEMS.register("card_redstone", CardRedstone::new);

    //Filters
    public static final RegistryObject<Item> Filter_Basic = ITEMS.register("filter_basic", FilterBasic::new);
    public static final RegistryObject<Item> Filter_Count = ITEMS.register("filter_count", FilterCount::new);
    public static final RegistryObject<Item> Filter_Tag = ITEMS.register("filter_tag", FilterTag::new);
    public static final RegistryObject<Item> Filter_Mod = ITEMS.register("filter_mod", FilterMod::new);
    public static final RegistryObject<Item> Filter_NBT = ITEMS.register("filter_nbt", FilterNBT::new);

    //Misc
    public static final RegistryObject<Item> Logic_Chip_Raw = ITEMS.register("logic_chip_raw", LogicChipRaw::new);
    public static final RegistryObject<Item> Logic_Chip = ITEMS.register("logic_chip", LogicChip::new);

    //Upgrades
    public static final RegistryObject<Item> Overclocker_Card = ITEMS.register("overclocker_card", () -> new OverclockerCard(-1));
    public static final RegistryObject<Item> Overclocker_Node = ITEMS.register("overclocker_node", OverclockerNode::new);
    //public static final RegistryObject<Item> overwrite = ITEMS.register("Test1",() -> new OverclockerCard(1) );
    //public static final RegistryObject<Item> overwrite = ITEMS.register("Test2",() -> new OverclockerCard(1) );
    //public static final RegistryObject<List<? extends Item>> list = ITEMS.register(ITEMS.("Test1",() -> new OverclockerCard(1)), ITEMS.register("Test2",() -> new OverclockerCard(1));
    //public static final RegistryObject<List<Item>> OC_LIST = List.of();
    //public static final String yes = "ohn";

    /*
    public static RegistryObject<Item> TEMP_TIERS;
    public void initEnergyTiers(int i) {
        int J extends Item item = 5;
        TEMP_TIERS = ITEMS.register(ENERGYTIERS_PATH.get().get(i), () -> new OverclockerCard(J));}
    */

    //Containers
    public static final RegistryObject<MenuType<LaserNodeContainer>> LaserNode_Container = CONTAINERS.register("lasernode",
            () -> IForgeMenuType.create((windowId, inv, data) -> new LaserNodeContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardItemContainer>> CardItem_Container = CONTAINERS.register("carditem",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardItemContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardFluidContainer>> CardFluid_Container = CONTAINERS.register("cardfluid",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardFluidContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardEnergyContainer>> CardEnergy_Container = CONTAINERS.register("cardenergy",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardEnergyContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardRedstoneContainer>> CardRedstone_Container = CONTAINERS.register("cardredstone",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardRedstoneContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardHolderContainer>> CardHolder_Container = CONTAINERS.register("cardholder",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardHolderContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<FilterBasicContainer>> FilterBasic_Container = CONTAINERS.register("filterbasic",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FilterBasicContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<FilterCountContainer>> FilterCount_Container = CONTAINERS.register("filtercount",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FilterCountContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<FilterTagContainer>> FilterTag_Container = CONTAINERS.register("filtertag",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FilterTagContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<FilterNBTContainer>> FilterNBT_Container = CONTAINERS.register("filternbt",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FilterNBTContainer(windowId, inv, inv.player, data)));

    // Conveniance function: Take a RegistryObject<Block> and make a corresponding RegistryObject<Item> from it
    //public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
    //    return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    //}
}
