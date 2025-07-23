package com.direwolf20.laserio.setup;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.events.ServerTickHandler;
import com.direwolf20.laserio.common.items.cards.CardFluid;
import com.direwolf20.laserio.common.network.PacketHandler;
import com.direwolf20.laserio.integration.ModIntegration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModSetup {
    public static final String TAB_NAME = LaserIO.MODID;
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LaserIO.MODID);
    public static final RegistryObject<CreativeModeTab> MOD_TAB = TABS.register(TAB_NAME, () -> CreativeModeTab.builder()
            .title(Component.literal(LaserIO.MODNAME))
            .icon(() -> new ItemStack(Registration.LASER_WRENCH.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((featureFlags, output) -> {
                Registration.ITEMS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);

                    if (item instanceof CardFluid) {
                        //If registered, place the Mekanism Card after the Fluid one
                        if (ModIntegration.MEKANISM.isLoaded()) {
                            Registration.MEKANISM_ITEMS.getEntries().forEach(f -> {
                                Item itemMek = f.get();
                                output.accept(itemMek);
                            });
                        }
                    }
                });
            })
            .build());

    public static void init(final FMLCommonSetupEvent event) {
        PacketHandler.register();
        MinecraftForge.EVENT_BUS.register(ServerTickHandler.class);
    }
}