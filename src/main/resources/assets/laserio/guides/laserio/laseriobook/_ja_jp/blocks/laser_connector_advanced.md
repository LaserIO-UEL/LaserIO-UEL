---
navigation:
  title: "発展中継器"
  icon: "laserio:laser_connector_advanced"
  position: 2
  parent: laserio:blocks/blocks.md
item_ids:
  - laserio:laser_connector_advanced
---

# 発展中継器

発展レーザー中継器は[レーザー中継器](./laser_connector.md)と同じように機能し、最大8ブロック離れた他の基本中継器又はノードに接続します。

ただし、他の発展中継器一つにのみ接続でき、ペアを形成します。一つの発展中継器からそのペアへの接続範囲は無限であり、ディメンションを跨ぐこともできます。

## 発展中継器

*相互に接続された2つの発展中継器*

![](laserio:assets/blocks/laser_connector_advanced/laser_connector_advanced1.png)

発展中継器にすでにペアが存在する状態で、新たに他の発展中継器と接続しようとすると、元の接続が切断され、新たなペアリングが形成されます。

[レーザーレンチ](../tools/laser_wrench.md)を持って中継器を見るとペアリングされた中継器の座標が表示されます。

## パートナー座標

*パートナー中継器の座標を表示*

![](laserio:assets/blocks/laser_connector_advanced/laser_connector_advanced2.png)

発展レーザー中継器はチャンクのロードは行いません。チャンクのロードはすべて自分で処理する必要があります。ロードされていないチャンク内のノードは動作しません。

## 発展レーザー中継器
<Recipe id="laserio:laser_connector_advanced" />

