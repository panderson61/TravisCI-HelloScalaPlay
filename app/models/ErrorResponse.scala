package models.api

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class ErrorResponse(errorMessage: String,
                         errorCode: Int)

object ErrorResponse {
  implicit val reads: Reads[ErrorResponse] = (
    (__ \ "errorMessage").read[String] and
      (__ \ "errorCode").read[Int]
    )(ErrorResponse.apply _)

  implicit val writes: Writes[ErrorResponse] = (
    (__ \ "errorMessage").write[String] and
      (__ \ "errorCode").write[Int]
    )(unlift(ErrorResponse.unapply))
}