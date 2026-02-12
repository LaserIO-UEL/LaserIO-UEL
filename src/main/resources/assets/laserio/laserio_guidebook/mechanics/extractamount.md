---
navigation:
  title: "Extract Amount"
  position: 7
  parent: laserio:mechanics/mechanics.md
---

# Extract Amount

Extract Amount is only available on Extractor and Stocker Modes.

This setting determines how many items (or how much fluid/fe) are extracted per operation.

For example, if set to 8 items per 20 ticks, the extractor will extract 8 items at a time, every 20 ticks.

Extractor and Stocker cards have a default value of 1, and a maximum value of 8, unless <ItemLink id="laserio:overclocker_card" /> are installed.  The following extract amounts are allowed with the designated overclockers installed:
1. 16
2. 32
3. 48
4. 64

If you want to extract 64 items at a time, you need 4 overclockers installed.

Left click on this value to increase it by 1. Right click to decrease it. Holding shift will multiply the change amount by 10, while holding control will multiply the change amount by 64.

These can be combined, meaning holding shift+ctrl will multiply by 640.

For example, shift right click will decrement by 10.

