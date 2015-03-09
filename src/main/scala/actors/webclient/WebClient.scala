package actors.webclient

import java.util.concurrent.Executor

import com.ning.http.client.AsyncHttpClient

import concurrent._


object WebClient {

  val client = new AsyncHttpClient()

  def get(url: String)(implicit exec: Executor): Future[String] = {
    val response = client.prepareGet(url).execute()
    val p = Promise[String]()
    response.addListener(new Runnable {
      override def run(): Unit = {
        val resp = response.get
        p.success(resp.getResponseBody)
      }
    }, exec)
    return p.future
  }

  val A_TAG = "(?i)<a ([^>]+)>.+?</a>".r
  val HREF_ATTR = ""

  def findLinks(body: String): Iterable[String] = {
    return List[String]("1", "2", "3")
  }


}
