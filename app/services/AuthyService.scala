package services

import java.io.FileInputStream
import java.net.URLDecoder

import models.User
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
  private[this] val MESSAGE = "message"
  private[this] val SECONDS_TO_EXPIRE = "seconds_to_expire"

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
    println("Calling " + authyUserRegistrationUrl.toString())

    val nameValuePairs = Map(
      (EMAIL, Seq(authyData.email)),
      (CELLPHONE, Seq(authyData.cellphone)),
      (COUNTRY_CODE, Seq(authyData.countryCode))
    )

    val authyFuture = WS.url(authyUserRegistrationUrl)
      .withHeaders(("X-Authy-API-Key", authyApiKey))
      .withRequestTimeout(authyTimeout)
      .post(nameValuePairs)

    val authyUserRegistrationResponse = Await.result(authyFuture, authyTimeout)

    authyUserRegistrationResponse.status match {
      case 200 => {
        val authyResponse = Json.parse(authyUserRegistrationResponse.body)
        val authyId = (authyResponse \ "user" \ "id").get.toString()
        //val authyId =(authyResponse\"user"\"id").asOpt.map(_.extract[String]).getOrElse("xxx")
        //val authyId = (authyResponse \\ "id").toString()
        //val authyUser = (authyResponse \ "user").as[String]
        //val message = (authyResponse \ "message").as[String]
        //(Json.parse(authyUserRegistrationResponse.body) \ "id").validate[List[String]] match {
        //  case s: JsSuccess[String] => authyId = s.get
        //  case e: JsError => println("Errors: " + JsError.toJson(e).toString())
        //}
        println("authService Registering Authy User")
        val authyUser = User(
          authyData.username,
          authyData.password,
          authyData.email,
          authyData.countryCode,
          authyData.cellphone,
          authyId,
          authyData.useAuthy
        )
        User.create(authyUser)
        AuthyUserRegistrationResponse(
          authyId = authyId
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
    println("Calling " + authySendSMSTokenUrl.toString())

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
          message = "unexpected error",
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
    println("authyCheckSMSTokenResponse status: " + authyCheckSMSTokenResponse.status.toString())
    println("authyCheckSMSTokenResponse body: " + authyCheckSMSTokenResponse.body.toString())

    authyCheckSMSTokenResponse.status match {
      case 200 => {
        val authyResponse = Json.parse(authyCheckSMSTokenResponse.body)

        val mySuccess = trimstr((authyResponse \ "success").get.toString())
        val myMessage = trimstr((authyResponse \ "message").get.toString())
        val myToken = trimstr((authyResponse \ "token").get.toString())

        println("check sms resp:" + mySuccess + ":" + myMessage + ":" + myToken)

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
          message = "unexpected error",
          token = "9876543210"
        )
      }
    }
  }

  def authySendOneTouch(authyData: AuthySendOneTouchRequest): AuthySendOneTouchResponse = {
    val authySendOneTouchUrl = authyUrl + "/onetouch/json/users/" + authyData.authyId + "/approval_requests"
    println("Calling " + authySendOneTouchUrl.toString())

    val nameValuePairs = Map(
      (MESSAGE, Seq(authyData.message)),
      (SECONDS_TO_EXPIRE, Seq(authyData.secondsToExpire))
    )

    val authyFuture = WS.url(authySendOneTouchUrl)
      .withHeaders(("X-Authy-API-Key", authyApiKey))
      .withRequestTimeout(authyTimeout)
      .post(nameValuePairs)

    val authySendOneTouchResponse = Await.result(authyFuture, authyTimeout)
    println(authySendOneTouchResponse.status.toString())
    println(authySendOneTouchResponse.body.toString())

    authySendOneTouchResponse.status match {
      case 200 => {
        val authyResponse = Json.parse(authySendOneTouchResponse.body)
        val mySuccess = trimstr((authyResponse \ "success").get.toString())
        val myUuid = trimstr((authyResponse \ "approval_request" \ "uuid").get.toString())
        println("resp:" + mySuccess + ":" + myUuid)

        AuthySendOneTouchResponse(
          mySuccess,
          myUuid
        )
      }
      case _ => {
        AuthySendOneTouchResponse(
          success = "false",
          uuid = "9876543210"
        )
      }
    }
  }

  def authyCheckOneTouch(authyBody: String) : String = {
    println("CheckOneTouch: " + authyBody)
    val authyResponse = Json.parse(authyBody)
    val myAuthyId = trimstr((authyResponse \ "authy_id").get.toString())
    val myDeviceUuid = trimstr((authyResponse \ "device_uuid").get.toString())
    val myStatus = trimstr((authyResponse \ "status").get.toString())
    println("ID: " + myAuthyId + "UUID: " + myDeviceUuid + "Status: " + myStatus)
    authyRequestValidator(myAuthyId, myDeviceUuid, myStatus)
  }

  def authyRequestValidator(authyId: String, Uuid: String, status: String): String = {
    "true"
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
