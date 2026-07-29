---
navigation:
  title: "Energy Card Subnets"
  parent: laserio:advanced/advanced.md
---

# Energy Card Subnets

<ItemLink id="laserio:card_energy" /> are capable of “subnetting”. That is, two separate <ItemLink id="laserio:laser_node" /> networks can transfer energy between each other. This is done by:
1. Placing two <ItemLink id="laserio:laser_node" /> beside each other 
2. Use an inserting <ItemLink id="laserio:card_energy" /> which pushes energy from the main network. 
3. Use an extracting <ItemLink id="laserio:card_energy" /> in the subnetwork which receives energy.

*The blue network pushes energy into the green network*

![](laserio:assets/advanced/energy_subnet/energy_subnetting_example.png)

#### *Why would I do this?*

Large networks are laggy. However, large networks that have only energy cards are **not** laggy. It is ideal to have one massive energy network, then push that into networks which require other types of [Cards](../cards/cards.md).

<ItemLink id="laserio:card_redstone" /> can also have subnets for more complex redstone operations.
