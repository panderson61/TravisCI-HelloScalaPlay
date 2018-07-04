package services

import java.net.URLDecoder

import models.authy._
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object AuthyService {

  private[this] val EMAIL = "email"
  private[this] val CELLPHONE = "cellphone"
  private[this] val COUNTRY_CODE = "country_code"
  private[this] val AUTHYID = "string"

  //private[this] val email = play.Play.application.configuration.getString("panderson@avetta.com")
  private[this] val email = "panderson@avetta.com"
  private[this] val cellphone = "7146146687"
  private[this] val country_code = "1"

  val authyUrl = "https://api.authy.com"
  val authyTimeout = Duration(20, "seconds")

  def authyUserRegistration(authyData: AuthyUserRegistrationRequest): AuthyUserRegistrationResponse = {
    val authyUserRegistrationUrl = authyUrl + "/protected/json/users/new"

    val nameValuePairs = Map(
      (EMAIL, Seq(email)),
      (CELLPHONE, Seq(cellphone)),
      (COUNTRY_CODE, Seq(country_code))
    )

    val authyFuture = WS.url(authyUserRegistrationUrl)
      .withRequestTimeout(authyTimeout)
      .post(nameValuePairs)

    val authyUserRegistrationResponse = Await.result(authyFuture, authyTimeout)

    authyUserRegistrationResponse.status match {
      case 200 => {
        val status = parseResults(authyUserRegistrationResponse.body).get("ACK").asInstanceOf[Some[String]].get
        status match {
          case "Success" => {
            val authyId = parseResults(authyUserRegistrationResponse.body).get("AUTHYID").asInstanceOf[Some[String]].get
            AuthyUserRegistrationResponse(
              authyId = authyId
            )
          }

          case _ => {
            val regex = "L_LONGMESSAGE".r
            val errorMsgs = parseResults(authyUserRegistrationResponse.body).filterKeys(regex.pattern.matcher(_).matches).values.toVector
            throw new Exception(s"Authy Errors => ${errorMsgs.mkString(" ~ ")}")
          }
        }
      }
      case _ => throw new Exception("Authy Error - Unable to reach server")
    }
  }

  private def parseResults(response: String): Map[String, String] = {
    response.split("&").map(result => {
      val nameValuePair = result.split("=")
      (nameValuePair.headOption.getOrElse(""), URLDecoder.decode(nameValuePair(1), "UTF-8"))
    }).toMap
  }

}
