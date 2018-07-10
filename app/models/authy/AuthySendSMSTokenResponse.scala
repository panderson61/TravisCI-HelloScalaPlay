package models.authy

import play.api.libs.json._

case class AuthySendSMSTokenResponse(
                                     success: String,
                                     message: String,
                                     cellphone: String
                                    )

object AuthySendSMSTokenResponse {

  implicit val authySendSMSTokenResponseFormat = Json.format[AuthySendSMSTokenResponse]

}