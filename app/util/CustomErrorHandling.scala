package util

import models.ErrorResponse
import org.slf4j.LoggerFactory
import play.api.libs.json._
import play.api.mvc.{Controller, Result}

trait CustomErrorHandling extends Controller {

  val logger = LoggerFactory.getLogger(classOf[CustomErrorHandling])

  def apiExceptionHandler(exception: Throwable, serviceName: String): Result = {

    logger.error("Error: Something bad.", exception)

    exception match {
      case w: ServiceException => w.httpCode match {
        case 400 => BadRequest(Json.toJson(ErrorResponse(w.getMessage,w.serviceName.toUpperCase + "." + w.picsCode.id)))
        case 404 => NotFound(Json.toJson(ErrorResponse(w.getMessage,w.serviceName.toUpperCase + "." + w.picsCode.id)))
        case _ => InternalServerError(Json.toJson(ErrorResponse(w.getMessage,w.serviceName.toUpperCase + "." + w.picsCode.id)))
      }
      case e: Throwable => InternalServerError(Json.toJson(ErrorResponse(e.getMessage,serviceName + "." + ErrorCodes.generalError.id)))
      case _ => InternalServerError(Json.toJson(ErrorResponse("Unknown exception.",serviceName + "." + ErrorCodes.generalError.id)))
    }
  }
}

object ErrorCodes extends Enumeration {

  type ErrorCodes = Value
  val generalError = Value(1)
  val inputJsonError = Value(2)

  implicit val enumReads: Reads[ErrorCodes] = EnumJson.enumReads(ErrorCodes)
  implicit def enumWrites: Writes[ErrorCodes] = EnumJson.enumWrites
}

object HttpCodes {
  val statusOkCode = 200
  val statusCreatedCode = 201
  val badRequestErrorCode = 400
  val notFoundErrorCode = 404
  val internalErrorCode = 500
}
