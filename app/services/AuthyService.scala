package services

import java.net.URLDecoder

import play.api.libs.json._
import models.authy._
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object AuthyService {

  private[this] val EMAIL = "user[email]"
  private[this] val CELLPHONE = "user[cellphone]"
  private[this] val COUNTRY_CODE = "user[country_code]"
  private[this] val AUTHYID = "string"

  //private[this] val email = play.Play.application.configuration.getString("panderson@avetta.com")
  private[this] val email = "panderson61@yahoo.com"
  private[this] val cellphone = "7146146687"
  private[this] val country_code = "1"
  private[this] val authyApiKey = play.Play.application.configuration.getString("avetta_auth_api_key")

  val authyUrl = "https://api.authy.com"
  val authyTimeout = Duration(20, "seconds")

  def authyUserRegistration(authyData: AuthyUserRegistrationRequest): AuthyUserRegistrationResponse = {
    //val authyUserRegistrationUrl = authyUrl + "/protected/json/users/new"
    val authyUserRegistrationUrl = "https://api.authy.com/protected/json/users/new"

    val nameValuePairs = Map(
      (EMAIL, Seq(email)),
      (CELLPHONE, Seq(cellphone)),
      (COUNTRY_CODE, Seq(country_code))
    )

    val authyFuture = WS.url(authyUserRegistrationUrl)
      .withHeaders(("X-Authy-API-Key", authyApiKey))
      .withRequestTimeout(authyTimeout)
      .post(nameValuePairs)

    val authyUserRegistrationResponse = Await.result(authyFuture, authyTimeout)

    authyUserRegistrationResponse.status match {
      case 200 => {
        //var authyId = "init"
        //val status = parseResults(authyUserRegistrationResponse.body).get("ACK").asInstanceOf[Some[String]].get
        //status match {
        //case "Success" => {
        //val authyId = parseResults(authyUserRegistrationResponse.body).get("authyId").asInstanceOf[Some[String]].get
        val authyResponse = Json.parse(authyUserRegistrationResponse.body)
        //val authyId = (authyResponse \ "user" \ "id").as[String]
        //val authyId = (authyResponse \ "user" \ "id").toString()
        val authyId = (authyResponse \ "user" \ "id").get.toString()
        //val authyId =(authyResponse\"user"\"id").asOpt.map(_.extract[String]).getOrElse("xxx")
        //val authyId = (authyResponse \\ "id").toString()
        //val authyUser = (authyResponse \ "user").as[String]
        //val message = (authyResponse \ "message").as[String]
        //(Json.parse(authyUserRegistrationResponse.body) \ "id").validate[List[String]] match {
        //  case s: JsSuccess[String] => authyId = s.get
        //  case e: JsError => println("Errors: " + JsError.toJson(e).toString())
        //}
        AuthyUserRegistrationResponse(
          authyId = authyId
          //authyId = authyUser
          //authyId = message
        )
      }
      case _ => {
        AuthyUserRegistrationResponse(
          authyId = authyUserRegistrationResponse.body
        )
      }
        //  }

        //  case _ => {
        //    val regex = "L_LONGMESSAGE".r
        //    val errorMsgs = parseResults(authyUserRegistrationResponse.body).filterKeys(regex.pattern.matcher(_).matches).values.toVector
        //    throw new Exception(s"Authy Errors => ${errorMsgs.mkString(" ~ ")}")
        //  }
        //}
      //case _ => throw new Exception("Authy Error - Unable to reach server")
    }
  }

  private def parseResults(response: String): Map[String, String] = {
    response.split("&").map(result => {
      val nameValuePair = result.split("=")
      (nameValuePair.headOption.getOrElse(""), URLDecoder.decode(nameValuePair(1), "UTF-8"))
    }).toMap
  }

}
