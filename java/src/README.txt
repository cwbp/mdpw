MDPW Ver2
主な変更点
Javaからも使えるようにAPIを公開
Note, Chord, Seq, Binary
JDK1.3のMIDI関連のクラスを使用するよう変更
バイナリデータも書けるよう変更 data
DataNote pitch = DataNote.UNUSED 
getByteArray();
setByteArray();
データ構造のうちPlayerに渡されるものが2種類に増えた
これ以上増えないだろうと予測し、DataNoteのサブクラスで
ごまかした。


Mのシンボルと数値の対応
Unusedの取り扱い
前の情報を探す、前がない場合はデフォルト
ノートOFFをノートオンのVelcity0を使っているが大ジョブか

TODO:
AudioBytePlayertcl結合テスト
AudioClipPlayerテスト
DataParser 
DataBinary作成
MIDIFilePlayer作成
MIDIRealtimePlayer作成 Sequencer のチェック
