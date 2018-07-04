package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._
import play.api.Logger
import _root_.util.{ErrorCodes, RequestLogging}
import models.framework.{EmptyServiceResponse, WsException}
import models.authy.AuthyUserRegistrationRequest
import models.User
import services.AuthyService
import views.html

import scala.util.control.Exception

object Authy extends Controller with RequestLogging {

  val logger = Logger(this.getClass)

  def userRegistration = RequestLoggingAction {
    request =>
      try {
        val authyData = Json.fromJson[AuthyUserRegistrationRequest](request.body.asJson.get).get
        val authyResponse = AuthyService.authyUserRegistration(authyData)
        Ok(Json.toJson(authyResponse))
      }
      catch {
        case e: Any => {
          exceptionHandler(e)
        }
      }
  }

  private def exceptionHandler(exception: Any): Result = {

    //Log exception
    Exception.ignoring(classOf[Any]) {
      exception match {
        case e: RuntimeException => requestLogger.error (e.getMessage)
      }
    }

    exception match {
      case w: WsException => w.httpCode match {
        case 400 => BadRequest(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String](w.getMessage),Option[Int](w.picsCode.id))))
        case 404 => NotFound(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String](w.getMessage),Option[Int](w.picsCode.id))))
        case _ => InternalServerError(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String](w.getMessage),Option[Int](w.picsCode.id))))
      }
      case e: Exception => InternalServerError(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String](e.getMessage),Option[Int](ErrorCodes.generalError.id))))
      case _ => InternalServerError(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String]("Unknown exception."),Option[Int](ErrorCodes.generalError.id))))
    }
  }
}
