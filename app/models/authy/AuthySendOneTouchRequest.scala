package models.authy

import play.api.libs.json._

case class AuthySendOneTouchRequest(authyId: String,
                                    message: String,
//                                    showDetails1: String,
//                                    hideDetails1: String,
                                    secondsToExpire: String
                                   )


object AuthySendOneTouchRequest {
  implicit val authySendOneTouchFormat = Json.format[AuthySendOneTouchRequest]
}