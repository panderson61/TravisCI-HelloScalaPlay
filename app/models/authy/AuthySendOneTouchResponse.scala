package models.authy

import play.api.libs.json._

case class AuthySendOneTouchResponse(
                                      success: String,
                                      uuid: String
                                    )

object AuthySendOneTouchResponse {

  implicit val authySendOneTouchResponseFormat = Json.format[AuthySendOneTouchResponse]

}