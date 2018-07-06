package models.authy

import play.api.libs.json._

case class AuthyUserRegistrationResponse(authyId: String)

object AuthyUserRegistrationResponse {

  implicit val authyUserRegistrationResponseFormat = Json.format[AuthyUserRegistrationResponse]

}