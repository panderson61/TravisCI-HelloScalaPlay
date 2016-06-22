package util

import models.api.ErrorResponse
import play.api.mvc.{Controller, Result}
import play.api.libs.json.Json


trait CustomErrorHandling extends Controller {

  def apiExceptionHandler(exception: Exception): Result = {

    //Log exception
    //    Exception.ignoring(classOf[Any]) {
    //      exception match {
    //        case e: RuntimeException => requestLogger.error (e.getMessage)
    //      }
    //    }

    exception match {
      case w: ServiceException => w.httpCode match {
        case 400 => BadRequest(Json.toJson(ErrorResponse(w.getMessage,w.picsCode.id)))
        case 404 => NotFound(Json.toJson(ErrorResponse(w.getMessage,w.picsCode.id)))
        case _ => InternalServerError(Json.toJson(ErrorResponse(w.getMessage,w.picsCode.id)))
      }
      case e: Exception => InternalServerError(Json.toJson(ErrorResponse(e.getMessage,ErrorCodes.GeneralError.id)))
      case _ => InternalServerError(Json.toJson(ErrorResponse("Unknown exception.",ErrorCodes.GeneralError.id)))
    }
  }

}

object ErrorCodes extends Enumeration {
  import play.api.libs.json._

  type ErrorCodes = Value
  val GeneralError = Value(1)
  val InputJsonError = Value(2)
  val CircularFormulaReference = Value(20)

  implicit val enumReads: Reads[ErrorCodes] = EnumJson.enumReads(ErrorCodes)
  implicit def enumWrites: Writes[ErrorCodes] = EnumJson.enumWrites
}
