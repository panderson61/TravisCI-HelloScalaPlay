package models.authy

import play.api.libs.json._

case class AuthySendSMSTokenRequest(email: String,
                                        cellphone: String,
                                        countryCode: String
                                       )


object AuthySendSMSTokenRequest {
  implicit val authySendSMSTokenRequestFormat = Json.format[AuthySendSMSTokenRequest]
}
