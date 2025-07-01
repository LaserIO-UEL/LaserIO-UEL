package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.ModSetup;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class LaserIOLanguage extends LanguageProvider {
    public LaserIOLanguage(PackOutput output, String locale) {
        super(output, LaserIO.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        //Items and blocks names
        add("itemGroup." + ModSetup.TAB_NAME, LaserIO.MODNAME);
        add(Registration.LaserConnector.get(), "Laser Connector");
        add(Registration.LaserConnectorAdv.get(), "Advanced Laser Connector");
        add(Registration.LaserNode.get(), "Laser Node");
        add(Registration.Laser_Wrench.get(), "Laser Wrench");
        add(Registration.Card_Holder.get(), "Card Holder");
        add(Registration.Card_Cloner.get(), "Card Cloner");
        add(Registration.Card_Item.get(), "Item Card");
        add(Registration.Card_Fluid.get(), "Fluid Card");
        add(Registration.Card_Energy.get(), "Energy Card");
        add(Registration.Card_Redstone.get(), "Redstone Card");
        add(Registration.Card_Chemical.get(), "Chemical Card");
        add(Registration.Filter_Basic.get(), "Basic Filter");
        add(Registration.Filter_Count.get(), "Counting Filter");
        add(Registration.Filter_Tag.get(), "Tag Filter");
        add(Registration.Filter_Mod.get(), "Mod Filter");
        add(Registration.Filter_NBT.get(), "NBT Filter");
        add(Registration.Logic_Chip.get(), "Logic Chip");
        add(Registration.Logic_Chip_Raw.get(), "Raw Logic Chip");
        add(Registration.Overclocker_Node.get(), "Node Overclocker");
        add(Registration.Logistic_Overclocker_Card.get(), "Logistic Overclocker");

        //Screens informations
        add("screen.laserio.extractamt", "Transfer Amount");
        add("screen.laserio.tickSpeed", "Speed (Ticks)");

        add("screen.laserio.priority", "Priority");
        add("screen.laserio.channel", "Channel: ");
        add("screen.laserio.redstonechannel", "Redstone Channel: ");
        add("screen.laserio.regulate", "Regulate");
        add("screen.laserio.roundrobin", "Round Robin: ");
        add("screen.laserio.true", "True");
        add("screen.laserio.false", "False");
        add("screen.laserio.enforced", "Enforced");
        add("screen.laserio.exact", "Exact");
        add("screen.laserio.and", "And");
        add("screen.laserio.or", "Or");
        add("screen.laserio.allowlist", "Allow");
        add("screen.laserio.comparenbt", "NBT");
        add("screen.laserio.lasernode", "Laser Node");
        add("screen.laserio.energylimit", "Energy Limit (%)");

        add("screen.laserio.default", "Default");
        add("screen.laserio.up", "Up");
        add("screen.laserio.down", "Down");
        add("screen.laserio.north", "North");
        add("screen.laserio.south", "South");
        add("screen.laserio.west", "West");
        add("screen.laserio.east", "East");
        add("screen.laserio.network_settings", "Network Settings");
        add("screen.laserio.apply", "Apply");
        add("screen.laserio.red", "Red");
        add("screen.laserio.green", "Green");
        add("screen.laserio.blue", "Blue");
        add("screen.laserio.alpha", "Alpha");
        add("screen.laserio.wrench", "Wrench Alpha");

        add("screen.laserio.extract", "Extract");
        add("screen.laserio.insert", "Insert");
        add("screen.laserio.stock", "Stock");
        add("screen.laserio.sensor", "Sensor");
        add("screen.laserio.input", "Input");
        add("screen.laserio.output", "Output");
        add("screen.laserio.weak", "Weak");
        add("screen.laserio.strong", "Strong");
        add("screen.laserio.redstoneMode", "Redstone: ");
        add("screen.laserio.ignored", "Ignored");
        add("screen.laserio.low", "Low");
        add("screen.laserio.high", "High");

        add("screen.laserio.redstone.interval", "Interval");
        add("screen.laserio.redstone.interval.lower_bound", "Lower bound");
        add("screen.laserio.redstone.interval.upper_bound", "Upper bound");
        add("screen.laserio.redstone.interval.output", "Output");
        add("screen.laserio.redstone.output_mode.normal", "Normal");
        add("screen.laserio.redstone.output_mode.complementary", "Complementary");
        add("screen.laserio.redstone.output_mode.not", "NOT");
        add("screen.laserio.redstone.logic_operation.none", "No logic operation");
        add("screen.laserio.redstone.logic_operation.and", "AND");
        add("screen.laserio.redstone.logic_operation.or", "OR");
        add("screen.laserio.redstone.logic_operation.xor", "XOR");

        add("screen.laserio.showparticles", "Show Particles");
        add("screen.laserio.hideparticles", "Hide Particles");

        add("screen.laserio.denylist", "Deny");
        add("screen.laserio.nbttrue", "Match NBT");
        add("screen.laserio.nbtfalse", "Ignore NBT");

        //General tooltips
        add("laserio.tooltip.item.show_details", "Hold shift to show details");
        add("laserio.tooltip.item.show_settings.shift_key", "Hold shift to show settings");
        add("laserio.tooltip.item.show_settings.ctrl_key", "Hold ctrl to show settings");
        add("laserio.tooltip.item.keys.left_click", "L-Click");
        add("laserio.tooltip.item.keys.right_click", "R-Click");
        add("laserio.tooltip.item.keys.shift_right_click", "Shift + R-Click");

        //Laser Wrench tooltips
        add("laserio.tooltip.item.laser_wrench.select_node", "Select Node: ");
        add("laserio.tooltip.item.laser_wrench.connect_node", "Connect Node: ");
        add("laserio.tooltip.item.laser_wrench.autoconnect_node", "Auto-connect Node: ");
        add("laserio.tooltip.item.laser_wrench.autoconnect_node.keys", "Offhand Wrench + Place Node");

        //Card Holder tooltips
        add("laserio.tooltip.item.card_holder.open", "Open: ");
        add("laserio.tooltip.item.card_holder.toggle_pulling", "Toggle pulling: ");

        //Card Cloner tooltips
        add("laserio.tooltip.item.card_cloner.in_node_ui", "In Node UI:");
        add("laserio.tooltip.item.card_cloner.in_node_ui.copy_card", "Copy Card: ");
        add("laserio.tooltip.item.card_cloner.in_node_ui.paste_card", "Paste Card: ");
        add("laserio.tooltip.item.card_cloner.in_world", "In world:");
        add("laserio.tooltip.item.card_cloner.in_world.copy_node", "Copy Node: ");
        add("laserio.tooltip.item.card_cloner.in_world.paste_node", "Paste Node: ");
        add("laserio.tooltip.item.card_cloner.in_world.clear", "Clear: ");
        add("laserio.tooltip.item.card_cloner.in_world.change_paste_mode", "Change paste mode: ");
        add("laserio.tooltip.item.card_cloner.copied_card", "Copied Card: ");
        add("laserio.tooltip.item.card_cloner.copied_node", "Copied Node: ");
        add("laserio.tooltip.item.card_cloner.copied_node.position", "Position: ");
        add("laserio.tooltip.item.card_cloner.copied_node.dimension", "Dimension: ");

        //Cards tooltips
        add("laserio.tooltip.item.card.mode", "Mode: ");
        add("laserio.tooltip.item.card.channel", "Channel: ");
        add("laserio.tooltip.item.card.mode.EXTRACT", "Extract");
        add("laserio.tooltip.item.card.mode.INSERT", "Insert");
        add("laserio.tooltip.item.card.mode.STOCK", "Stock");
        add("laserio.tooltip.item.card.mode.SENSOR", "Sensor");
        add("laserio.tooltip.item.card.sneaky", "Sneaky: ");
        add("laserio.tooltip.item.card.sneaky.DOWN", "Down");
        add("laserio.tooltip.item.card.sneaky.UP", "Up");
        add("laserio.tooltip.item.card.sneaky.NORTH", "North");
        add("laserio.tooltip.item.card.sneaky.SOUTH", "South");
        add("laserio.tooltip.item.card.sneaky.WEST", "West");
        add("laserio.tooltip.item.card.sneaky.EAST", "East");
        add("laserio.tooltip.item.card.Filter", "Filter: ");
        add("laserio.tooltip.item.card.Overclocker", "Overclocker: ");
        add("laserio.tooltip.item.card.Overclockers", "Overclockers: ");
        add("laserio.tooltip.item.card.None", "None");

        //Filters tooltips
        add("laserio.tooltip.item.filter.type", "Type: ");
        add("laserio.tooltip.item.filter.type.allow", "Allow");
        add("laserio.tooltip.item.filter.type.deny", "Deny");
        add("laserio.tooltip.item.filter.nbt", "Match NBT: ");
        add("laserio.tooltip.item.filter.nbt.allow", "True");
        add("laserio.tooltip.item.filter.nbt.deny", "False");

        //Energy Overclockers tooltip
        add("laserio.tooltip.item.energy_overclocker.max_fe", "Max %d FE/operation");

        //Laser Wrench client messages
        add("message.laserio.laser_wrench.exceeded_maximum_connection_range", "Connection exceeds maximum range of %d!");

        //Card Holder client messages
        add("message.laserio.card_holder.pulling", "Card Holder pulling: ");
        add("message.laserio.card_holder.pulling.enabled", "Enabled");
        add("message.laserio.card_holder.pulling.disabled", "Disabled");

        //Card Cloner client messages
        add("message.laserio.card_cloner.paste_mode", "Paste mode: ");
        add("message.laserio.card_cloner.paste_mode.network_settings", "Network settings");
        add("message.laserio.card_cloner.paste_mode.node_contents", "Node contents");
        add("message.laserio.card_cloner.copy_node_first", "Copy a Node before pasting!");
        add("message.laserio.card_cloner.node_copied", "Node copied!");
        add("message.laserio.card_cloner.network_settings_pasted", "Network settings pasted!");
        add("message.laserio.card_cloner.insufficient_materials", "Insufficient materials to paste!");
        add("message.laserio.card_cloner.node_contents_pasted", "Node contents pasted!");
        add("message.laserio.card_cloner.stored_settings_cleared", "Stored settings cleared!");

        //Keybinds
        add("key.laserio.card_holder.open", "Open Card Holder");
        add("key.laserio.card_holder.toggle_pulling", "Toggle Card Holder Pulling");

        //Curios Card Holder slot
        add("curios.identifier.card_holder", "Card Holder");

        //add("", "");
    }
}