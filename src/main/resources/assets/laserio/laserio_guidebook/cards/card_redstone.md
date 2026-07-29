---
navigation:
  title: "Redstone Card"
  icon: "laserio:card_redstone"
  position: 4
  parent: laserio:cards/cards.md
item_ids:
  - laserio:card_redstone
---

# Redstone Card

Redstone Cards are used to transmit redstone signals across the entire LaserIO network. 

Redstone Cards have a dedicated [redstone channel](../mechanics/redstonechannel.md), separate from the channels that other cards use. 

All cards have a [redstone mode](../mechanics/redstonemode.md) toggle, which defaults to ignored, meaning the cards will always operate.

Redstone cards have 2 modes:

**Input mode** will accept a redstone signal from something like redstone dust, a lever, or a button, and transmit it across the network on the redstone channel configured on the card.

**Output mode** will emit a redstone signal to blocks like redstone dust, lamp, or repeaters.

Output mode has a toggle for Weak vs Strong. In weak mode, only directly adjacent blocks like redstone will get the signal, similar to how redstone dust works.

In strong mode the redstone signal can transmit through an adjacent block and affect the block on the other side, like how levers work.

---

## Advanced Settings

Some settings exist for more complex redstone needs:

**Input mode**

Interval mode will reveal three other buttons: Lower bound, Upper bound, and Output.

The Lower and Upper bounds determine when (Upper bound => signal => Lower bound) a signal will be sent on the redstone channel.
The signal strength will be equal to the specified Output when activated.

**Output mode**

Complementary takes the channel signal and emits: 15 - that.
NOT is similar to Complementary but only gives off a 15 signal strength when there is none on the channel. Any signal from the channel and nothing will be emitted.

Lastly, the 'Logic Operation' mode. These settings utilize another redstone channel to perform a boolean operation with the already chosen one.

The possible operations are documented in the following page.

Logic Operation mode offers three types of boolean operators: OR, AND, XOR.

OR emits a signal when either channel has power.
AND only emits a signal when both channels are powered.
XOR emits a signal when exactly one of the two channels has power.

Logic Operation settings can be combined with Complementary/NOT to get their negated version (NOR, NAND, XNOR)!

## Redstone Card



<RecipesFor fallbackText="" id="laserio:card_redstone" />

