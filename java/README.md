# アプリケーション概要（Abstract）
本アプリケーションは古典平均場アニーリング反復法を使用した画像修復アプリケーションです。
コードはJava SE6をベースにしたswingで実装しています。

<img src="https://github.com/Hermiltonian/image-restoring/blob/master/captured_image.png">

# 使い方（Usage）
## 起動方法（How to activate)
ImagePanel.javaをEclipse等のIDEで実行してください。または、コンパイル済みのアプリケーションにて`java ImageRestoring`と実行してください。

## 操作方法（How to operate)
1. デフォルトで原画像が表示されるので、「劣化させる」ボタンを押下してください。
2. 左上の劣化確率で示す確率で各マスが反転し、原画像が劣化した画像が表示されます。
3. 「画像修復」ボタンを押下してください。画面右上のパラメータに従って劣化した画像から原画像を推定します。
4. ラジオボタンを選択すれば、原画像を好きなように修正可能です。

# バージョン情報（Version）
1.1.1
