# リンク

* [Dispatch Github](https://github.com/n8han/Databinder-Dispatch)

* [Dispatchチュートリアル](http://dispatch.databinder.net/Dispatch.html)

* [Dispatch APIレファレンス](http://databinder.net/dispatch-doc/#package)

* [Twitterサンプル](https://github.com/n8han/dispatch-twitter)

# このサムプルのUsage

    sbt console
    
    :load src/scala/Atnd.scala
    
    // @shibuyascalaが登録したAtndイベントのIDを取得する
    getEventsOwnedByUser("shibuyascala")
    
    // @cbirchallが参加したAtndイベントのIDを取得する
    getEventsAttendedByUser("cbirchall")
    
    // @shibuyascalaが登録したAtndイベントの名前を取得する（文字化けする）
    getEventTitles(getEventsOwnedByUser("shibuyascala"))

# Dispatchについて

## 基本的な使い方

    val result = <executor>(<request> <response handler>)

## Executor

* HTTP（同一スレッド）

依存性：dispatch-http

内部でApache HttpClient使用

    val h = new dispatch.Http
    val result: String = h(url("http://google.com/") as_str)

* HTTP（別スレッド）

依存性：dispatch-http

    val h = new dispatch.thread.Http
    val result: Future[String] = h(url("http://google.com/") as_str)

* HTTP（nio）

依存性：dispatch-nio

    val h = new dispatch.nio.Http
    val result: Future[String] = h(url("http://google.com/") as_str)

* Google App Engine専用

依存性：dispatch-gae

    val h = new dispatch.gae.Http
    val result: String = h(url("http://google.com/") as_str)

### Futureについて

返ってくる`Future`を`apply()`すれば結果が得られる。

`Future`の状態を確認するには`isSet`を呼び出す。

## Request

### URL

* `url()`メソッドを使う

    val url: Request = url("http://example.com/a/b/")

* ホストとパスを組み合わせる

    val url: Request = :/("example.com") / "a" / "b"

### Request verbs

リクエストを変換するための演算子。

* `<:<` HTTPヘッダを指定

* `<<` HTTP POSTのボディを指定

* `<<<` HTTP PUTのボディを指定

* `<<?` URLクエリパラメータを付ける

* `>\` 文字コードを設定

* `as` HTTP BASIC/DIGEST認証のユーザ名とパスワードを指定

* `gzip` Gzip圧縮を頼む

* `secure` HTTPSを使う

例）

    val req = url("http://example.com/a/b/") <<? Map("user" -> "chris")

## Response handlers

返ってくるHTTPレスポンスを処理するハンドラ。

* `>|` レスポンスを無視する

* `as_str` ボディをStringとして返す

* `>-` ボディをStringとして扱って処理する

* `as_source` ボディをscala.io.Sourceとして返す

* `>~` ボディをscala.io.Sourceとして扱って処理する

* `>:>` ヘッダをMapとして扱って処理する

* `>>>` ボディをOutputStreamに書き込む

* `<>` ボディをXMLとして扱って処理する

* `</>` ボディをXHTMLとして扱って処理する

## json

依存性：dispatch-json, dispatch-http-json

ハンドラ`>#`を使う。

例）

    import dispatch.json._
    import dispatch.json.JsHttp._
    
    val result = url("http://example.com/a/b/") <# { 
      list ! str
    }

正直、このextractorの使い方良く分かってない。lift-json使おう。

## lift-json

依存性：dispatch-lift-json, dispatch-http-json（たしか必要）

同上、`>#`を使うけど、ブロックの中にlift-jsonのASTが使える。

## その他

* dispatch-futures: Futureですごいことできそう

* dispatch-mime: mime-type関連

* dispatch-oauth: OAuth認証を楽にしてくれる

* http://github.com/n8han 以下：S3連携、CouchDB連携、。。。


