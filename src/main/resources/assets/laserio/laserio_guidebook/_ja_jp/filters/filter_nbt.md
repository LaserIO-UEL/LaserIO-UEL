---
navigation:
  title: "NBTフィルター"
  icon: "laserio:filter_nbt"
  position: 3
  parent: laserio:filters/filters.md
item_ids:
  - laserio:filter_nbt
---

# NBTフィルター

NBTフィルターは、<ItemLink id="laserio:filter_tag" />と非常によく似たUIと機能を備えています。

右上のスロットにアイテムを配置すると、そのアイテムのすべてのNBTタグラベルが表示されます。それらをリストに追加すると (タグフィルターと同じ方法で)、それらのタグに割り当てられている値に関係なく、それらのタグを含むアイテムがフィルターされます。

## NBT UI

*NBT UI*

![](laserio:assets/filters/filter_nbt/nbt_filter.png)

たとえば、すべてのエンチャントされたアイテムには「エンチャント」タグが付いており、ドロップ増加、ダメージ増加などが指定されています。このリストに「エンチャント」タグを追加すると、フィルターによってすべてのエンチャントされたアイテムが除外されます。

エンチャントされた剣は1つのインベントリに入れ、エンチャントされていない剣は別のインベントリに入れることができます。どのエンチャントが適用されているかに関係なく。

その他のすべての機能は <ItemLink id="laserio:filter_tag" /> と同じです。詳細については、その項目を参照してください。

