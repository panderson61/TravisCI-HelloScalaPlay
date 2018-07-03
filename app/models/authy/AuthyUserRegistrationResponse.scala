package models.authy

import play.api.libs.json._

case class AuthyUserRegistrationResponse(token: String)

object AuthyUserRegistrationResponse {

  implicit val AuthyUserRegistrationResponseFormat = Json.format[AuthyUserRegistrationResponse]

}