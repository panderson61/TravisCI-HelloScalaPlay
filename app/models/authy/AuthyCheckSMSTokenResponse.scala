package models.authy

import play.api.libs.json._

case class AuthyCheckSMSTokenResponse(
                                       success: String,
                                       message: String,
                                       token: String
                                     )

object AuthyCheckSMSTokenResponse {

  implicit val authyCheckSMSTokenResponseFormat = Json.format[AuthyCheckSMSTokenResponse]

}