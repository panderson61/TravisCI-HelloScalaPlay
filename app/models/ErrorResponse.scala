package models

import play.api.libs.json._

case class ErrorResponse(message: String, code: String)

object ErrorResponse {
  implicit val errorResponseFormat = Json.format[ErrorResponse]
}