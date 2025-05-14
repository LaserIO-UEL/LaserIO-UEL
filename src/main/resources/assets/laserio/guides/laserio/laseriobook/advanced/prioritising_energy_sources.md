---
navigation:
  title: "Prioritizing Energy Sources"
  icon: "laserio:card_energy"
  parent: laserio:advanced/advanced.md
---

# Prioritizing Energy Sources

You may find yourself in a situation where you want certain energy sources to be used before others. 

**For this tutorial I have a primary and secondary power source.**

#### *How do I ensure the secondary source drains before the primary?*

[Stock mode](../mechanics/modes.md) provides a solution:

1. On the destination, use an [Energy Card](../cards/card_energy.md) on **Stock** mode
2. On both the primary and secondary source, use an [Energy Card](../cards/card_energy.md) on **Insert** mode
3. Set the priority of the secondary source to be higher than the priority on the primary source

NOTE: only two sources were used in this example, but you can use this for more than two sources.