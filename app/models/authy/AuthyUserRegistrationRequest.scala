package models.authy

import play.api.libs.json._

case class AuthyUserRegistrationRequest(email: String,
                                        cellphone: String,
                                        countryCode: String,
                                        token: String
)


object AuthyUserRegistrationRequest {
  implicit val authyUserRegistrationFormat = Json.format[AuthyUserRegistrationRequest]
}
