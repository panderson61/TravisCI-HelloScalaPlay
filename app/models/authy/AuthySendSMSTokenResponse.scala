package models.authy

import play.api.libs.json._

case class AuthySendSMSTokenResponse(authyId: String)

object AuthySendSMSTokenResponse {

  implicit val authySendSMSTokenResponseFormat = Json.format[AuthySendSMSTokenResponse]

}