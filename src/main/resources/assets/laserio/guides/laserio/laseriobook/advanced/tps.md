---
navigation:
  title: "Performance T(i)PS"
  icon: "minecraft:clock"
  parent: laserio:advanced/advanced.md
---

# Performance T(i)PS

#### *What is TPS?*

Ticks per second (TPS), is the speed at which the game runs at. When the game is running at normal speed, 20 ticks occur in every one second.

#### *Why does this matter?*

Various factors can cause the TPS to drop below 20, making the game slower. For example, 10 TPS would cause every furnace recipe to take twice as long. If you have ever encountered mined block taking a few seconds to drop, that is the game running at a low TPS.

#### *How do I measure TPS?*

There is a built-in forge command, "/forge tps". There are also more accurate diagnostic tools such as [spark](https://www.curseforge.com/minecraft/mc-mods/spark).

## LaserIO and TPS

Everything you do causes some sort of TPS impact. Some very negligible, and others… not so much. Two basic rules of thumb are: 
- **Only use what you need** 
- **The faster the process, the greater the TPS impact**

In terms of LaserIO, this means that you should **not** be using the minimum fully overclocked [Tick Speed](../mechanics/tickspeed.md) when it is not needed. Increasing the [Extract Amount](../mechanics/extractamount.md) is insignificant compared to decreasing the [Tick Speed](../mechanics/tickspeed.md).

When using LaserIO (or any other logistic mod), ask yourself:
1. Is the default speed sufficient?
2. If not, increase the [Extract Amount](../mechanics/extractamount.md), is it sufficient now?
3. If it is still insufficient, decrease the [Tick Speed](../mechanics/tickspeed.md) until it is sufficient.

NOTE: [Energy Cards](../cards/card_energy.md) do **NOT** follow the guidelines above. You should **not worry about them**.

This is only a partial explanation, if you are interested in learning more, see the [Minecraft Wiki](https://minecraft.wiki/w/Tick) 

