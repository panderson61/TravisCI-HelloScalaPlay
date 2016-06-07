import collection.mutable.Stack
import org.scalatestplus.play._
import play.api.test._

//Unit Testing Controllers
import scala.concurrent.Future
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.Helpers._
import controllers.Application

class ApplicationControllerSpec extends PlaySpec with Results {

  "Application Index" should {
    "should return Hello World" in {
      val controller = new Application
      val result: Future[Result] = controller.index.apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "Hello World"
    }
  }
}


//Default Play Framework Test (Does not work with Travis CI?)
/*import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready.")
    }
  }
}
*/