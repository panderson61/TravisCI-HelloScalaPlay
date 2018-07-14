package models.authy

import play.api.libs.json._

case class AuthyUserRegistrationRequest(username: String,
                                        password: String,
                                        email: String,
                                        cellphone: String,
                                        countryCode: String,
                                        useAuthy: String
)


object AuthyUserRegistrationRequest {
  implicit val authyUserRegistrationFormat = Json.format[AuthyUserRegistrationRequest]
}
