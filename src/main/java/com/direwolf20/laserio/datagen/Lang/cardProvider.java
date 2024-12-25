package com.direwolf20.laserio.datagen.Lang;


import com.direwolf20.laserio.setup.Registration;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import static com.direwolf20.laserio.setup.ModSetup.TAB_NAME;

public class cardProvider {
    public static  void init(RegistrateLangProvider provider) {
        provider.add("itemGroup." + TAB_NAME, "LaserIO");
        provider.add(Registration.LaserConnector.get(), "Laser Connector");
        provider.add(Registration.LaserConnectorAdv.get(), "Advanced Laser Connector");
        provider.add(Registration.LaserNode.get(), "Laser Node");
        provider.add(Registration.Laser_Wrench.get(), "Laser Wrench");
        provider.add(Registration.Card_Holder.get(), "Card Holder");
        provider.add(Registration.Card_Item.get(), "Item Card");
        provider.add(Registration.Card_Fluid.get(), "Fluid Card");
        provider.add(Registration.Card_Energy.get(), "Energy Card");
        provider.add(Registration.Card_Redstone.get(), "Redstone Card");
        provider.add(Registration.Card_Chemical.get(), "Chemical Card");
        provider.add(Registration.Filter_Basic.get(), "Basic Filter");
        provider.add(Registration.Filter_Count.get(), "Counting Filter");
        provider.add(Registration.Filter_Tag.get(), "Tag Filter");
        provider.add(Registration.Filter_Mod.get(), "Mod Filter");
        provider.add(Registration.Filter_NBT.get(), "NBT Filter");
        provider.add(Registration.Logic_Chip.get(), "Logic Chip");
        provider.add(Registration.Logic_Chip_Raw.get(), "Raw Logic Chip");
        provider.add(Registration.Overclocker_Card.get(), "Card Overclocker");
        provider.add(Registration.Overclocker_Node.get(), "Node Overclocker");
        provider.add(Registration.Card_Cloner.get(), "Card Cloner");

        provider.add("lio_overclock_tooltip_lang", "§7 smth smth insert number %s here");
        provider.add("screen.laserio.extractamt", "Transfer Amount");
        provider.add("screen.laserio.tickSpeed", "Speed (Ticks)");

        provider.add("screen.laserio.priority", "Priority");
        provider.add("screen.laserio.channel", "Channel: ");
        provider.add("screen.laserio.redstonechannel", "Redstone Channel: ");
        provider.add("screen.laserio.regulate", "Regulate");
        provider.add("screen.laserio.roundrobin", "Round Robin: ");
        provider.add("screen.laserio.true", "True");
        provider.add("screen.laserio.false", "False");
        provider.add("screen.laserio.enforced", "Enforced");
        provider.add("screen.laserio.exact", "Exact");
        provider.add("screen.laserio.and", "And");
        provider.add("screen.laserio.or", "Or");
        provider.add("screen.laserio.allowlist", "Allow");
        provider.add("screen.laserio.comparenbt", "NBT");
        provider.add("screen.laserio.lasernode", "Laser Node");
        provider.add("screen.laserio.energylimit", "Energy Limit (%)");

        provider.add("screen.laserio.default", "Default");
        provider.add("screen.laserio.up", "Up");
        provider.add("screen.laserio.down", "Down");
        provider.add("screen.laserio.north", "North");
        provider.add("screen.laserio.south", "South");
        provider.add("screen.laserio.west", "West");
        provider.add("screen.laserio.east", "East");
        provider.add("screen.laserio.settings", "Settings");
        provider.add("screen.laserio.apply", "Apply");
        provider.add("screen.laserio.red", "Red");
        provider.add("screen.laserio.green", "Green");
        provider.add("screen.laserio.blue", "Blue");
        provider. add("screen.laserio.alpha", "Alpha");
        provider.add("screen.laserio.wrench", "Wrench Alpha");

        provider.add("screen.laserio.extract", "Extract");
        provider.add("screen.laserio.insert", "Insert");
        provider.add("screen.laserio.stock", "Stock");
        provider.add("screen.laserio.sensor", "Sensor");
        provider.add("screen.laserio.input", "Input");
        provider.add("screen.laserio.output", "Output");
        provider.add("screen.laserio.weak", "Weak");
        provider.add("screen.laserio.strong", "Strong");
        provider.add("screen.laserio.redstoneMode", "Redstone: ");
        provider.add("screen.laserio.ignored", "Ignored");
        provider.add("screen.laserio.low", "Low");
        provider.add("screen.laserio.high", "High");

        provider.add("screen.laserio.showparticles", "Show Particles");
        provider.add("screen.laserio.hideparticles", "Hide Particles");

        provider.add("screen.laserio.denylist", "Deny");
        provider.add("screen.laserio.nbttrue", "Match NBT");
        provider.add("screen.laserio.nbtfalse", "Ignore NBT");

        provider.add("message.laserio.wrenchrange", "Connection exceeds maximum range of %d");

        //Card Tooltips
        provider.add("laserio.tooltip.item.show_settings", "Hold shift to show settings");
        provider.add("laserio.tooltip.item.card.mode", "Mode: ");
        provider.add("laserio.tooltip.item.card.channel", "Channel: ");
        provider.add("laserio.tooltip.item.card.mode.EXTRACT", "Extract");
        provider.add("laserio.tooltip.item.card.mode.INSERT", "Insert");
        provider.add("laserio.tooltip.item.card.mode.STOCK", "Stock");
        provider.add("laserio.tooltip.item.card.mode.SENSOR", "Sensor");
        provider.add("laserio.tooltip.item.card.sneaky", "Sneaky: ");
        provider.add("laserio.tooltip.item.card.sneaky.DOWN", "Down");
        provider.add("laserio.tooltip.item.card.sneaky.UP", "Up");
        provider.add("laserio.tooltip.item.card.sneaky.NORTH", "North");
        provider.add("laserio.tooltip.item.card.sneaky.SOUTH", "South");
        provider.add("laserio.tooltip.item.card.sneaky.WEST", "West");
        provider.add("laserio.tooltip.item.card.sneaky.EAST", "East");
        provider.add("laserio.tooltip.item.card.Filter", "Filter: ");
        provider.add("laserio.tooltip.item.card.Overclockers", "Overclockers: ");
        provider.add("laserio.tooltip.item.card.None", "None");

        //Filter Tooltips
        provider.add("laserio.tooltip.item.filter.type", "Type: ");
        provider.add("laserio.tooltip.item.filter.type.allow", "Allow");
        provider.add("laserio.tooltip.item.filter.type.deny", "Deny");
        provider.add("laserio.tooltip.item.filter.nbt", "Match NBT: ");
        provider.add("laserio.tooltip.item.filter.nbt.allow", "True");
        provider.add("laserio.tooltip.item.filter.nbt.deny", "False");

    }
}
