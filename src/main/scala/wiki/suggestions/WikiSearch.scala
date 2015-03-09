package wiki.suggestions

import com.google.gson.annotations.SerializedName
import retrofit._
import retrofit.client._
import retrofit.http._

import concurrent.Promise
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async._
import scala.collection.JavaConverters._


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

  def wikiPageImpl(page: String, service: WikiService): Future[String] = {
    async {
      println("getting page: " + page)
      val (ft, cb) = callbackFuture[Page]
      service.page(page, cb)
      val result = await {
        ft
      }
      result.parse.text.all
    }
  }

  def wikiSugguestionsImpl(term: String, service: WikiService): Future[List[AnyRef]] = {
    async {
      println("searching: " + term)
      val (ft, cb) = callbackFuture[Array[AnyRef]]
      service.suggestions(term, cb)
      val result = await {
        ft
      }
      val arraylist = result(1).asInstanceOf[java.util.List[String]]
      println("res: " + arraylist.asScala.toList.size)
      arraylist.asScala.toList
    }
  }

  val restAdapter = new RestAdapter.Builder().setEndpoint("http://en.wikipedia.org").build()
  val service = restAdapter.create(classOf[WikiService])


  def wikiSuggestions(term: String) = wikiSugguestionsImpl(term, service)

  def wikiPage(page: String) = wikiPageImpl(page, service)

  def main(args: Array[String]) = {

    val rs = wikiSugguestionsImpl("test", service)

  }
}
