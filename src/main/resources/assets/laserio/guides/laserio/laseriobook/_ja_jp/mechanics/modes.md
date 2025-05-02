---
navigation:
  title: "モード"
  icon: "laserio:textures/gui/buttons/modestocker.png"
  position: 1
  parent: laserio:mechanics.md
---

# モード

モードは、カードの基本的な動作を決定します。次のページでは、現在利用可能な各モードタイプを定義します。

各カードタイプ (アイテム/液体/エネルギー) は、次の3つのモードをサポートします。アイテムカードは例として使用されています。

レッドストーンカードは、異なるモードセットをサポートします。

<ItemImage id="laserio:card_item{channel:0b" />
<ItemImage id="exact:0b" />
<ItemImage id="inv:{Items:[]" />
<ItemImage id="" />
<ItemImage id="itemextractamt:1b" />
<ItemImage id="itemextractspeed:20" />
<ItemImage id="mode:0b" />
<ItemImage id="priority:0s" />
<ItemImage id="regulate:0b" />
<ItemImage id="roundRobin:0" />
<ItemImage id="" />

搬入モードカードは、搬出モードカードが抽出したオブジェクトの有効な宛先となります。

ストックモードカードは、搬入モードカードからデータを取得しようとします。

<ItemImage id="laserio:card_item{channel:0b" />
<ItemImage id="exact:0b" />
<ItemImage id="inv:{Items:[]" />
<ItemImage id="" />
<ItemImage id="itemextractamt:1b" />
<ItemImage id="itemextractspeed:20" />
<ItemImage id="mode:1b" />
<ItemImage id="priority:0s" />
<ItemImage id="regulate:0b" />
<ItemImage id="roundRobin:0" />
<ItemImage id="" />

搬出モードカードは、隣接するブロックからオブジェクトを取り出そうとします。たとえば、アイテムは隣接するチェストから取り出され、搬入カードに送られます。

<ItemImage id="laserio:card_item{channel:0b" />
<ItemImage id="exact:0b" />
<ItemImage id="inv:{Items:[]" />
<ItemImage id="" />
<ItemImage id="itemextractamt:1b" />
<ItemImage id="itemextractspeed:20" />
<ItemImage id="mode:2b" />
<ItemImage id="priority:0s" />
<ItemImage id="regulate:0b" />
<ItemImage id="roundRobin:0" />
<ItemImage id="" />

ストックモードカードは、フィルターで指定されたアイテムを探し、同じネットワーク内の他の搬入ノードからそれらを引き出そうとします。

ストックモードカードには、フィルターが「許可」に設定されている必要があります。

<ItemImage id="laserio:card_item{channel:0b" />
<ItemImage id="exact:0b" />
<ItemImage id="inv:{Items:[]" />
<ItemImage id="" />
<ItemImage id="itemextractamt:1b" />
<ItemImage id="itemextractspeed:20" />
<ItemImage id="mode:3b" />
<ItemImage id="priority:0s" />
<ItemImage id="regulate:0b" />
<ItemImage id="roundRobin:0" />
<ItemImage id="" />

センサーカードはオブジェクトを移動させることはありませんが、隣接するインベントリを確認し、そのインベントリがフィルターと一致すると、レッドストーンチャンネルでレッドストーン信号を発信します。

センサーモードカードにはフィルターが必要です。

