package models.authy

import play.api.libs.json._

case class AuthySendSMSTokenRequest(authyId: String
                                   )


object AuthySendSMSTokenRequest {
  implicit val authySendSMSTokenRequestFormat = Json.format[AuthySendSMSTokenRequest]
}
