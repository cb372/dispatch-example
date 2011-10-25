def withShutdown[T](h:{def shutdown()})(f: => T) = {
  try {
    f
  } finally {
    h.shutdown()
  }
}

import dispatch._

// XML, 同一スレッド
def getEventsOwnedByUser(nickname: String): Seq[Int] = {
  val h = new Http
  withShutdown(h) {
    val u = url("http://api.atnd.org/events/")
    val withParams = u <<? Map(
      "owner_nickname" -> nickname,
      "format" -> "xml"
    )
    val nodes = h(withParams <> { 
      _ \\ "event_id" 
    })
    return nodes.map(_.text.toInt)
  }
}

// json, 別スレッド
def getEventsAttendedByUser(nickname: String): Seq[Int] = {
  import dispatch.json._
  import dispatch.json.JsHttp._

  val h = new thread.Http 
  withShutdown(h) {
    val u = :/("api.atnd.org") / "events/"
    val withParams = u <<? Map(
      "nickname" -> nickname,
      "format" -> "json"
    )
    val future = h(withParams ># { 
      ('events ! (list ! obj))
    })
    val id = 'event_id ? num
    while (!future.isSet) { 
      println("Waiting for results...")
      Thread.sleep(50)
    }
    return future.apply().map(id).map(_.toInt)
  }
}

// lift-json, 別スレッド
def getEventsAttendedByUser_liftJson(nickname: String) = {
  import dispatch.liftjson.Js._
  import net.liftweb.json.JsonAST._

  val h = new thread.Http 
  withShutdown(h) {
    val u = :/("api.atnd.org") / "events/"
    val withParams = u <<? Map(
      "nickname" -> nickname,
      "format" -> "json"
    )
    val future = h(withParams ># { json => 
      (json \ "events" \\ "event_id" children) flatMap (_ match {
        case JField("event_id", JInt(id)) => Some(id)
        case _ => None
      })
    })
    while (!future.isSet) { 
      println("Waiting for results...")
      Thread.sleep(50)
    }
    future.apply()
  }
}

// 汚いHTMLスクリーンスクレープ, nio
def getEventTitles(eventIds: Seq[Int]): Seq[String] = {
  val h = new nio.Http
  withShutdown(h) {
    val u = :/("atnd.org") / "events"
    val futures = eventIds.map(id => 
      h(u / id.toString >- { str =>
        import java.util.regex._
        val m = Pattern.compile("<title>(.*) : ATND</title>").matcher(str)
        m.find
        m.group(1)
      })
    )
    return futures.map(_.apply)
  }
}

