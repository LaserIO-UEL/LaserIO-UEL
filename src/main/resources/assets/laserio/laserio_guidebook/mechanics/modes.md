---
navigation:
  title: "Modes"
  position: 1
  parent: laserio:mechanics/mechanics.md
---

# Modes

Modes determine what a card fundamentally does. The following pages define each mode type currently available.

Each type of card (Item/Fluid/Energy) supports the the following three modes. Item cards are used as an example.

<ItemLink id="laserio:card_redstone" /> will support a different set of modes.

Insert Mode cards are a validate destination for objects being extracted by Extract Mode cards.

Stock mode cards will attempt to pull from Insert Mode cards.

<ItemImage id="laserio:card_item" />

Extract Mode cards attempt to remove objects from their adjacent block. Items, for example, will be removed from an adjacent chest and sent to an insert card.

<ItemImage id="laserio:card_item" tag="{mode:1b}"/>

Stock Mode cards attempt to find the designated items in their filter, and pull them from other Insert Nodes in the same network.

Stock mode cards require a filter set to Allow.

<ItemImage id="laserio:card_item" tag="{mode:2b}"/>

Sensor cards do not move objects around, but instead look at an adjacent inventory, and will emit a redstone signal on the redstone channel if the inventory matches the filter.

Sensor mode cards require a filter.

<ItemImage id="laserio:card_item" tag="{mode:3b}"/>
