package util

import scala.concurrent.Future
import scala.util.matching.Regex
import play.api.Logger
import play.api.mvc._

trait RequestLogging {

  val requestLogger = Logger("request")

  object RequestLoggingAction extends ActionBuilder[Request] {

    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      requestLogger.debug(s"method=${request.method} uri=${request.uri} body=${LoggingHelper.maskCCNumber(request.body.toString)}")
      block(request)
    }
  }
}

object LoggingHelper {

  //Mask the cc number in the request before logging for PCI compliance
  def maskCCNumber(inputText: String): String = {
    inputText.replaceAll("\"creditCardNumber\":\"(.*?)\"","\"creditCardNumber\":\"************\"")
  }

}