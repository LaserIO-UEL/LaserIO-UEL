---
navigation:
  title: "Prioritizing Extraction"
  parent: laserio:advanced/advanced.md
---

# Prioritizing Extraction

You may find yourself in a situation where you want to have a chest/tank/energy source empty **before** another chest/tank/energy source respectively.

[Stock mode](../mechanics/modes.md) provides a solution. Stock mode essentially "pulls" from Cards on insert mode. Traditionally, extract mode Cards "push" to Cards on insert mode. 

This opens up many operations to be used in a "reversed" way. For example, insert cards have a [priority setting](../mechanics/priority.md), this only applies to the destination. When using stocking mode, this now applies to the source.

#### *Tutorial: Ensuring the secondary source drains before the primary*

1. On the destination, use an [Energy Card](../cards/card_energy.md) on **Stock** mode
2. On both the primary and secondary source, use an [Energy Card](../cards/card_energy.md) on **Insert** mode
3. Set the priority of the secondary source to be higher than the priority on the primary source

NOTE: only two sources were used in this example, but you can use this for more than two sources.

[A more detailed example.](https://github.com/LaserIO-UEL/LaserIO-UEL/issues/41)