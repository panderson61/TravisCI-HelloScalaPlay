package models.authy

import play.api.libs.json._

case class AuthyUserRegistrationResponse(authyId: String)

object AuthyUserRegistrationResponse {

  implicit val AuthyUserRegistrationResponseFormat = Json.format[AuthyUserRegistrationResponse]

}