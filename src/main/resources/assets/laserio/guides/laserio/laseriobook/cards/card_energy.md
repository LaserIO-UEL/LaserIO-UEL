---
navigation:
  title: "Energy Cards"
  icon: "laserio:card_energy"
  position: 3
  parent: laserio:cards/cards.md
item_ids:
  - laserio:card_energy
---

# Energy Cards

Energy Cards are used to send Energy between inventories, such as machines and batteries.

Energy cards have some slightly different mechanics from the other cards, and will be documentated in the following pages.

Unlike Item/Fluid cards that require overclockers to operate faster than once every 20 ticks, Energy Cards can operate every tick by default.

Overclockers can't go into Energy cards. Energy cards always operate at 1,000,000 FE/tick max. This can be reduced if you wish.

---

## Limit

Limit % only exists on Energy Cards. Limit % specifies the percentage of FE to operate with on the adjacent energy accepting block.

There are two different metrics, Insert % for Stock/Insert mode, and Extract % for extract mode.

**Insert/Stock Limit**

By default, the limit% is set to 100%.  Specifies how much to fill up the specified energy block.

For example: If the block can hold 1,000,000 FE, and you specify 50%, it will only fill up to 500,000FE.

The default (100%) will fill it up completely.

**Extract Limit**

By default, the limit% is set to 0%. Specifies how much to leave behind in the specified energy block.

For example: If the block can hold 1,000,000 FE, and you specify 50%, it will extract until the block has 500,000fe remaining, and will not extract any more.

The default (0%) will extract all energy.

Technical Note: The Forge Energy system supports energy storage up to MAX_INT which is approximately 2.14 billion FE. If you are using a mod like Draconic Evolution or Mekanism, their storage cells can store greater than this amount by 'hacking' how Forge Energy works. As a result, this % indicator will not work on storage cells greater than 2.14 billion FE. Sorry! :)

## Energy Card



<Recipe id="laserio:card_energy" />

