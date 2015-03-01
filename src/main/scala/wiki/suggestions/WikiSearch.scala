package wiki.suggestions

import com.google.gson.annotations.SerializedName
import retrofit._
import retrofit.client._
import retrofit.http._

import concurrent.Promise
import scala.concurrent.Future

/**
 * Created by aliaksandr on 3/1/15.
 */
object WikiSearch {

  class Page {
    var parse: Content = _
  }

  class Content {
    var title: String = _
    var text: Text = _
  }

  class Text {
    @SerializedName("*")
    var all: String = _
  }

  trait WikiService {
    @GET("/w/api.php?action=opensearch&format=json&limit=15")
    def suggestions(@Query("search") term: String, callback: Callback[Array[AnyRef]]): Unit


    @GET("/w/api.php?action=parse&format=json&prop=text&section=0")
    def page(@Query("page") term: String, callback: Callback[Page]): Unit

  }

  def callbackFuture[T](): (Future[T], Callback[T]) = {

    val p = Promise[T]()
    val cb = new Callback[T] {

      override def success(page: T, response: Response): Unit = {
        p success page
      }

      override def failure(error: RetrofitError): Unit = {
        p failure error
      }
    }

    return (p.future, cb)
  }

  def main(args: Array[String]) = {
    val restAdapter = new RestAdapter.Builder().setEndpoint("http://en.wikipedia.org").build()
    val service = restAdapter.create(classOf[WikiService])

    val cb = new Callback[Page] {
      override def success(t: Page, response: Response): Unit = {
        println(t.parse.title)
        println(t.parse.text.all)
      }

      override def failure(retrofitError: RetrofitError): Unit = {
        println("BAAD :(  " + retrofitError.getUrl)
      }
    }

    service.page("1", cb)

  }
}
