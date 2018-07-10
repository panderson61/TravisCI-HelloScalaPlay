package services

import java.io.FileInputStream
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
  private[this] val VIA = "via"
  private[this] val LOCALE = "locale"
  private[this] val VERIFICATION_CODE = "verification_code"

  //private[this] val email = play.Play.application.configuration.getString("panderson@avetta.com")
  private[this] val email = "panderson61@yahoo.com"
  private[this] val cellphone = "7146146687"
  private[this] val country_code = "1"
  private[this] val via_sms = "sms"
  private[this] val locale_en = "en"

  private[this] val authyApiKey = play.Play.application.configuration.getString("avetta.auth.api.key")

  val authyUrl = "https://api.authy.com"
  val authyTimeout = Duration(20, "seconds")
  //val props = new java.util.Properties
  //props.load(new FileInputStream(Play.application.path + "/conf/.authy_key"))
  //private[this] val authyApiKey = props.getProperty(avetta_auth_api_key)

  def authyUserRegistration(authyData: AuthyUserRegistrationRequest): AuthyUserRegistrationResponse = {
    val authyUserRegistrationUrl = authyUrl + "/protected/json/users/new"

    //TODO build nameValuePairs from authyData
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

  def authySendSMSToken(authyData: AuthySendSMSTokenRequest): AuthySendSMSTokenResponse = {
    val authySendSMSTokenUrl = authyUrl + "/protected/json/sms/" + authyData.authyId + "?force=true"
    println(authySendSMSTokenUrl.toString())

    //TODO build nameValuePairs from authyData
    //val nameValuePairs = Map(
    //  (VIA, Seq(via_sms)),
    //  (LOCALE, Seq(locale_en)),
    //  (CELLPHONE, Seq(cellphone)),
    //  (COUNTRY_CODE, Seq(country_code))
    //)

    val authyFuture = WS.url(authySendSMSTokenUrl)
      .withHeaders(("X-Authy-API-Key", authyApiKey))
      .withRequestTimeout(authyTimeout)
      .get

    val authySendSMSTokenResponse = Await.result(authyFuture, authyTimeout)
    println(authySendSMSTokenResponse.status.toString())
    println(authySendSMSTokenResponse.body.toString())

    authySendSMSTokenResponse.status match {
      case 200 => {
        val authyResponse = Json.parse(authySendSMSTokenResponse.body)
      //  //TODO Parse needed response values
      //  val authyId = (authyResponse \ "user" \ "id").get.toString()
        AuthySendSMSTokenResponse(
          success = (authyResponse \ "success").get.toString(),
          message = (authyResponse \ "message").get.toString(),
          cellphone = (authyResponse \ "cellphone").get.toString()
        )
      }
      case _ => {
        AuthySendSMSTokenResponse(
          success = "false",
          message = "messageText",
          cellphone = "9876543210"
        )
      }
    }
  }

  def authyCheckSMSToken(authyData: AuthyCheckSMSTokenRequest): AuthyCheckSMSTokenResponse = {
    val authyCheckSMSTokenUrl = authyUrl + "/protected/json/verify/" + authyData.verificationCode + "/" + authyData.authyId

    val authyFuture = WS.url(authyCheckSMSTokenUrl)
      .withHeaders(("X-Authy-API-Key", authyApiKey))
      .withRequestTimeout(authyTimeout)
      .get

    val authyCheckSMSTokenResponse = Await.result(authyFuture, authyTimeout)
    println(authyCheckSMSTokenResponse.status.toString())
    println(authyCheckSMSTokenResponse.body.toString())

    authyCheckSMSTokenResponse.status match {
      case 200 => {
        val authyResponse = Json.parse(authyCheckSMSTokenResponse.body)

        val mySuccess = trimstr((authyResponse \ "success").get.toString())
        val myMessage = trimstr((authyResponse \ "message").get.toString())
        val myToken = trimstr((authyResponse \ "token").get.toString())

        println("resp:" + mySuccess + ":" + myMessage + ":" + myToken)

        //AuthyCheckSMSTokenResponse(
        //  success = (authyResponse \ "success").get.toString(),
        //  message = (authyResponse \ "message").get.toString(),
        //  token = (authyResponse \ "token").get.toString()
        //)
        AuthyCheckSMSTokenResponse(
          mySuccess,
          myMessage,
          myToken
        )
      }
      case _ => {
        AuthyCheckSMSTokenResponse(
          success = "false",
          message = "messageText",
          token = "9876543210"
        )
      }
    }
  }

  private def parseResults(response: String): Map[String, String] = {
    response.split("&").map(result => {
      val nameValuePair = result.split("=")
      (nameValuePair.headOption.getOrElse(""), URLDecoder.decode(nameValuePair(1), "UTF-8"))
    }).toMap
  }

  def trimstr(instr: String): String = {
    if (instr.length() >= 2 && instr.charAt(0) == '"' && instr.charAt(instr.length() - 1) == '"')
      instr.substring(1, instr.length() - 1)
    else
      instr
  }

}
