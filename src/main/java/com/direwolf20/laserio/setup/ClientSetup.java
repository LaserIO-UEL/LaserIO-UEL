package com.direwolf20.laserio.setup;

import com.direwolf20.laserio.client.blockentityrenders.LaserConnectorAdvBERender;
import com.direwolf20.laserio.client.blockentityrenders.LaserConnectorBERender;
import com.direwolf20.laserio.client.blockentityrenders.LaserNodeBERender;
import com.direwolf20.laserio.client.events.ClientEvents;
import com.direwolf20.laserio.client.events.EventTooltip;
import com.direwolf20.laserio.client.events.KeybindHandler;
import com.direwolf20.laserio.client.screens.CardEnergyScreen;
import com.direwolf20.laserio.client.screens.CardFluidScreen;
import com.direwolf20.laserio.client.screens.CardHolderScreen;
import com.direwolf20.laserio.client.screens.CardItemScreen;
import com.direwolf20.laserio.client.screens.CardRedstoneScreen;
import com.direwolf20.laserio.client.screens.FilterBasicScreen;
import com.direwolf20.laserio.client.screens.FilterCountScreen;
import com.direwolf20.laserio.client.screens.FilterNBTScreen;
import com.direwolf20.laserio.client.screens.FilterTagScreen;
import com.direwolf20.laserio.client.screens.LaserNodeScreen;
import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.blockentities.LaserConnectorAdvBE;
import com.direwolf20.laserio.common.blockentities.LaserConnectorBE;
import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.blockentities.basebe.BaseLaserBE;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.cards.BaseCard.TransferMode;
import com.direwolf20.laserio.common.items.cards.CardRedstone;
import com.direwolf20.laserio.integration.ModIntegration;
import com.direwolf20.laserio.integration.mekanism.client.screens.CardChemicalScreen;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.Color;

@Mod.EventBusSubscriber(modid = LaserIO.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    private static final ResourceLocation CARD_TRANSFER_MODE = new ResourceLocation(LaserIO.MODID, "mode");

    private static int getCardTransferMode(ItemStack stack, Level level, LivingEntity living, int id) {
        return BaseCard.getTransferMode(stack);
    }

    public static void init(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Registration.LASER_NODE_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.LASER_CONNECTOR_BLOCK.get(), RenderType.cutout());

        //Register our render events
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        MinecraftForge.EVENT_BUS.register(EventTooltip.class);

        //Register our keybinds handler
        MinecraftForge.EVENT_BUS.register(new KeybindHandler());

        //Screens
        event.enqueueWork(() -> {
            //Attach our containers to the screens
            MenuScreens.register(Registration.LASER_NODE_CONTAINER.get(), LaserNodeScreen::new);
            MenuScreens.register(Registration.CARD_ITEM_CONTAINER.get(), CardItemScreen::new);
            MenuScreens.register(Registration.CARD_FLUID_CONTAINER.get(), CardFluidScreen::new);
            MenuScreens.register(Registration.CARD_ENERGY_CONTAINER.get(), CardEnergyScreen::new);
            MenuScreens.register(Registration.CARD_REDSTONE_CONTAINER.get(), CardRedstoneScreen::new);
            MenuScreens.register(Registration.CARD_HOLDER_CONTAINER.get(), CardHolderScreen::new);
            MenuScreens.register(Registration.FILTER_BASIC_CONTAINER.get(), FilterBasicScreen::new);
            MenuScreens.register(Registration.FILTER_COUNT_CONTAINER.get(), FilterCountScreen::new);
            MenuScreens.register(Registration.FILTER_TAG_CONTAINER.get(), FilterTagScreen::new);
            MenuScreens.register(Registration.FILTER_NBT_CONTAINER.get(), FilterNBTScreen::new);
        });

        //Give the Cards an insert/extract on the ItemStack
        event.enqueueWork(() -> {
            ItemProperties.register(Registration.CARD_ITEM.get(), CARD_TRANSFER_MODE, ClientSetup::getCardTransferMode);
            ItemProperties.register(Registration.CARD_FLUID.get(), CARD_TRANSFER_MODE, ClientSetup::getCardTransferMode);
            ItemProperties.register(Registration.CARD_ENERGY.get(), CARD_TRANSFER_MODE, ClientSetup::getCardTransferMode);
            ItemProperties.register(Registration.CARD_REDSTONE.get(), CARD_TRANSFER_MODE, ClientSetup::getCardTransferMode);
        });

        //Mekanism
        if (ModIntegration.MEKANISM.isLoaded()) {
            event.enqueueWork(() -> {
                MenuScreens.register(Registration.CARD_CHEMICAL_CONTAINER.get(), CardChemicalScreen::new);
                ItemProperties.register(Registration.CARD_CHEMICAL.get(), CARD_TRANSFER_MODE, ClientSetup::getCardTransferMode);
            });
        }
    }

    //Register block-entity renderers
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.LASER_CONNECTOR_BE.get(), LaserConnectorBERender::new);
        event.registerBlockEntityRenderer(Registration.LASER_NODE_BE.get(), LaserNodeBERender::new);
        event.registerBlockEntityRenderer(Registration.LASER_CONNECTOR_ADV_BE.get(), LaserConnectorAdvBERender::new);
    }

    @SubscribeEvent
    public static void registerTooltipFactory(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(EventTooltip.CopyPasteTooltipComponent.Data.class, EventTooltip.CopyPasteTooltipComponent::new);
    }

    //Register keybinds
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeybindHandler.OPEN_CARD_HOLDER);
        event.register(KeybindHandler.TOGGLE_CARD_HOLDER_PULLING);
    }

    private static int getCardChannelColor(ItemStack stack, int index) {
        if (index != 2) {
            return 0xFFFFFFFF;
        }
        int colorIndex = (BaseCard.getNamedTransferMode(stack) == TransferMode.SENSOR) ? BaseCard.getRedstoneChannel(stack) : BaseCard.getChannel(stack);
        Color color = LaserNodeBERender.COLORS[colorIndex];
        return color.getRGB();
    }

    private static int getBlockItemColor(ItemStack stack, int index) {
        return (index == 1) ? 0xFFFF0000 : 0xFFFFFFFF;
    }

    @SubscribeEvent
    static void itemColors(RegisterColorHandlersEvent.Item event) {
        final ItemColors colors = event.getItemColors();

        //Give the Cards their channel color on the ItemStack
        colors.register(ClientSetup::getCardChannelColor, Registration.CARD_ITEM.get());
        colors.register(ClientSetup::getCardChannelColor, Registration.CARD_FLUID.get());
        colors.register(ClientSetup::getCardChannelColor, Registration.CARD_ENERGY.get());
        colors.register((stack, index) -> {
            if (index != 2) {
                return 0xFFFFFFFF;
            }
            Color color = LaserNodeBERender.COLORS[CardRedstone.getRedstoneChannel(stack)];
            return color.getRGB();
        }, Registration.CARD_REDSTONE.get());

        //Mekanism Card (registered only if Mekanism is loaded)
        if (ModIntegration.MEKANISM.isLoaded()) {
            colors.register(ClientSetup::getCardChannelColor, Registration.CARD_CHEMICAL.get());
        }

        //Give Nodes and Connectors their color on the ItemStack
        colors.register(ClientSetup::getBlockItemColor, Registration.LASER_NODE_ITEM.get());
        colors.register(ClientSetup::getBlockItemColor, Registration.LASER_CONNECTOR_ITEM.get());
        colors.register(ClientSetup::getBlockItemColor, Registration.LASER_CONNECTOR_ADV_ITEM.get());
    }

    private static int getBlockColor (Class<? extends BaseLaserBE> BEClass, BlockAndTintGetter env, BlockPos pos) {
        if (env == null || pos == null) {
            return 0xFFFF0000;
        }
        BlockEntity blockEntity = env.getBlockEntity(pos);
        if (!BEClass.isInstance(blockEntity)) {
            return 0xFFFF0000;
        }
        BaseLaserBE baseLaserBE = (BaseLaserBE) blockEntity;
        Color color = baseLaserBE.getColor();
        return color.getRGB();
    }

    //Give Nodes and Connectors their color when placed in world
    @SubscribeEvent
    public static void blockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, env, pos, index) -> getBlockColor(LaserNodeBE.class, env, pos),
                Registration.LASER_NODE_BLOCK.get()
        );
        event.register(
                (state, env, pos, index) -> getBlockColor(LaserConnectorBE.class, env, pos),
                Registration.LASER_CONNECTOR_BLOCK.get()
        );
        event.register(
                (state, env, pos, index) -> getBlockColor(LaserConnectorAdvBE.class, env, pos),
                Registration.LASER_CONNECTOR_ADV_BLOCK.get()
        );
    }
}