package models.authy

import play.api.libs.json._

case class AuthyCheckSMSTokenRequest(authyId: String,
                                     verificationCode: String
                                    )


object AuthyCheckSMSTokenRequest {
  implicit val authyCheckSMSTokenRequestFormat = Json.format[AuthyCheckSMSTokenRequest]
}
