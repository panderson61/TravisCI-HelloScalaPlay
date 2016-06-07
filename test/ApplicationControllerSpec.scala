import play.api.test._
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