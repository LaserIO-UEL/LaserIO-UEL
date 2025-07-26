package com.direwolf20.laserio.setup;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.blockentities.LaserConnectorAdvBE;
import com.direwolf20.laserio.common.blockentities.LaserConnectorBE;
import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.blocks.LaserConnector;
import com.direwolf20.laserio.common.blocks.LaserConnectorAdv;
import com.direwolf20.laserio.common.blocks.LaserNode;
import com.direwolf20.laserio.common.containers.CardEnergyContainer;
import com.direwolf20.laserio.common.containers.CardFluidContainer;
import com.direwolf20.laserio.common.containers.CardHolderContainer;
import com.direwolf20.laserio.common.containers.CardItemContainer;
import com.direwolf20.laserio.common.containers.CardRedstoneContainer;
import com.direwolf20.laserio.common.containers.FilterBasicContainer;
import com.direwolf20.laserio.common.containers.FilterCountContainer;
import com.direwolf20.laserio.common.containers.FilterNBTContainer;
import com.direwolf20.laserio.common.containers.FilterTagContainer;
import com.direwolf20.laserio.common.containers.LaserNodeContainer;
import com.direwolf20.laserio.common.items.CardCloner;
import com.direwolf20.laserio.common.items.CardHolder;
import com.direwolf20.laserio.common.items.LaserWrench;
import com.direwolf20.laserio.common.items.LogicChip;
import com.direwolf20.laserio.common.items.LogicChipRaw;
import com.direwolf20.laserio.common.items.cards.CardEnergy;
import com.direwolf20.laserio.common.items.cards.CardFluid;
import com.direwolf20.laserio.common.items.cards.CardItem;
import com.direwolf20.laserio.common.items.cards.CardRedstone;
import com.direwolf20.laserio.common.items.filters.FilterBasic;
import com.direwolf20.laserio.common.items.filters.FilterCount;
import com.direwolf20.laserio.common.items.filters.FilterMod;
import com.direwolf20.laserio.common.items.filters.FilterNBT;
import com.direwolf20.laserio.common.items.filters.FilterTag;
import com.direwolf20.laserio.common.items.upgrades.OverclockerCard;
import com.direwolf20.laserio.common.items.upgrades.OverclockerNode;
import com.direwolf20.laserio.datagen.customrecipes.CardClearRecipe;
import com.direwolf20.laserio.integration.ModIntegration;
import com.direwolf20.laserio.integration.mekanism.common.containers.CardChemicalContainer;
import com.direwolf20.laserio.integration.mekanism.common.items.cards.CardChemical;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.direwolf20.laserio.client.particles.ModParticles.PARTICLE_TYPES;
import static com.direwolf20.laserio.common.LaserIO.MODID;
import static com.direwolf20.laserio.integration.mekanism.client.MekanismModParticles.MEKANISM_PARTICLE_TYPES;

public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LaserIO.MODID);

    //DeferredRegisters dedicated to Mekanism (registered only if Mekanism is loaded)
    public static final DeferredRegister<Item> MEKANISM_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<MenuType<?>> MEKANISM_CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

    public static void init() {
        IntStream.range(1, Config.MAX_FE_TIERS.get().size() + 1)
                .forEach(i -> ENERGY_OVERCLOCKER_CARDS.add(
                        ITEMS.register("energy_overclocker_card_tier_" + i, () -> new OverclockerCard(i))
                ));

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        if (ModIntegration.MEKANISM.isLoaded()) {
            MEKANISM_ITEMS.register(bus);
            MEKANISM_CONTAINERS.register(bus);
            MEKANISM_PARTICLE_TYPES.register(bus);
        }
        BLOCK_ENTITIES.register(bus);
        CONTAINERS.register(bus);
        PARTICLE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }

    //Blocks
    public static final RegistryObject<LaserNode> LASER_NODE_BLOCK = BLOCKS.register("laser_node", LaserNode::new);
    public static final RegistryObject<Item> LASER_NODE_ITEM = ITEMS.register("laser_node", () -> new BlockItem(LASER_NODE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> LASER_CONNECTOR_BLOCK = BLOCKS.register("laser_connector", LaserConnector::new);
    public static final RegistryObject<Item> LASER_CONNECTOR_ITEM = ITEMS.register("laser_connector", () -> new BlockItem(LASER_CONNECTOR_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> LASER_CONNECTOR_ADV_BLOCK = BLOCKS.register("laser_connector_advanced", LaserConnectorAdv::new);
    public static final RegistryObject<Item> LASER_CONNECTOR_ADV_ITEM = ITEMS.register("laser_connector_advanced", () -> new BlockItem(LASER_CONNECTOR_ADV_BLOCK.get(), new Item.Properties()));

    //BlockEntities
    public static final RegistryObject<BlockEntityType<LaserNodeBE>> LASER_NODE_BE = BLOCK_ENTITIES.register("lasernode", () -> BlockEntityType.Builder.of(LaserNodeBE::new, LASER_NODE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<LaserConnectorBE>> LASER_CONNECTOR_BE = BLOCK_ENTITIES.register("laserconnector", () -> BlockEntityType.Builder.of(LaserConnectorBE::new, LASER_CONNECTOR_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<LaserConnectorAdvBE>> LASER_CONNECTOR_ADV_BE = BLOCK_ENTITIES.register("laserconnectoradv", () -> BlockEntityType.Builder.of(LaserConnectorAdvBE::new, LASER_CONNECTOR_ADV_BLOCK.get()).build(null));

    //Tools
    public static final RegistryObject<Item> LASER_WRENCH = ITEMS.register("laser_wrench", LaserWrench::new);
    public static final RegistryObject<Item> CARD_HOLDER = ITEMS.register("card_holder", CardHolder::new);
    public static final RegistryObject<Item> CARD_CLONER = ITEMS.register("card_cloner", CardCloner::new);

    //Cards
    public static final RegistryObject<Item> CARD_ITEM = ITEMS.register("card_item", CardItem::new);
    public static final RegistryObject<Item> CARD_FLUID = ITEMS.register("card_fluid", CardFluid::new);
    public static final RegistryObject<Item> CARD_ENERGY = ITEMS.register("card_energy", CardEnergy::new);
    public static final RegistryObject<Item> CARD_REDSTONE = ITEMS.register("card_redstone", CardRedstone::new);

    //Mekanism Card (registered only if Mekanism is loaded)
    public static final RegistryObject<Item> CARD_CHEMICAL = MEKANISM_ITEMS.register("card_chemical", CardChemical::new);

    //Filters
    public static final RegistryObject<Item> FILTER_BASIC = ITEMS.register("filter_basic", FilterBasic::new);
    public static final RegistryObject<Item> FILTER_COUNT = ITEMS.register("filter_count", FilterCount::new);
    public static final RegistryObject<Item> FILTER_TAG = ITEMS.register("filter_tag", FilterTag::new);
    public static final RegistryObject<Item> FILTER_MOD = ITEMS.register("filter_mod", FilterMod::new);
    public static final RegistryObject<Item> FILTER_NBT = ITEMS.register("filter_nbt", FilterNBT::new);

    //Upgrades
    public static final RegistryObject<Item> OVERCLOCKER_NODE = ITEMS.register("overclocker_node", OverclockerNode::new);
    public static final RegistryObject<Item> LOGISTIC_OVERCLOCKER_CARD = ITEMS.register("overclocker_card", () -> new OverclockerCard(-1));

    //Energy Overclockers (registered only if tiers are added using config)
    public static final List<RegistryObject<Item>> ENERGY_OVERCLOCKER_CARDS = new ArrayList<>();

    //Crafting components
    public static final RegistryObject<Item> LOGIC_CHIP_RAW = ITEMS.register("logic_chip_raw", LogicChipRaw::new);
    public static final RegistryObject<Item> LOGIC_CHIP = ITEMS.register("logic_chip", LogicChip::new);

    //Containers
    public static final RegistryObject<MenuType<LaserNodeContainer>> LASER_NODE_CONTAINER = CONTAINERS.register("lasernode",
            () -> IForgeMenuType.create((windowId, inv, data) -> new LaserNodeContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardItemContainer>> CARD_ITEM_CONTAINER = CONTAINERS.register("carditem",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardItemContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardFluidContainer>> CARD_FLUID_CONTAINER = CONTAINERS.register("cardfluid",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardFluidContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardEnergyContainer>> CARD_ENERGY_CONTAINER = CONTAINERS.register("cardenergy",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardEnergyContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardRedstoneContainer>> CARD_REDSTONE_CONTAINER = CONTAINERS.register("cardredstone",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardRedstoneContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<CardHolderContainer>> CARD_HOLDER_CONTAINER = CONTAINERS.register("cardholder",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardHolderContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<FilterBasicContainer>> FILTER_BASIC_CONTAINER = CONTAINERS.register("filterbasic",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FilterBasicContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<FilterCountContainer>> FILTER_COUNT_CONTAINER = CONTAINERS.register("filtercount",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FilterCountContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<FilterTagContainer>> FILTER_TAG_CONTAINER = CONTAINERS.register("filtertag",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FilterTagContainer(windowId, inv, inv.player, data)));
    public static final RegistryObject<MenuType<FilterNBTContainer>> FILTER_NBT_CONTAINER = CONTAINERS.register("filternbt",
            () -> IForgeMenuType.create((windowId, inv, data) -> new FilterNBTContainer(windowId, inv, inv.player, data)));

    //Mekanism container (registered only if Mekanism is loaded)
    public static final RegistryObject<MenuType<CardChemicalContainer>> CARD_CHEMICAL_CONTAINER = MEKANISM_CONTAINERS.register("cardchemical",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CardChemicalContainer(windowId, inv, inv.player, data)));

    //Recipe serializer
    public static final RegistryObject<CardClearRecipe.Serializer> CARD_CLEAR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("cardclear", CardClearRecipe.Serializer::new);
}