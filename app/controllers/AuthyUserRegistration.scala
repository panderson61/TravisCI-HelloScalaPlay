package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._
import views._
import models.User
import models.authy.{AuthyUserRegistrationRequest,AuthyUserRegistrationResponse}
import services.AuthyService

class AuthyUserRegistration extends Controller {

  /**
    * GET userRegistration form.
    */
  def getUserRegistrationForm = Action { implicit request =>
    Ok(html.userregistration(userRegistrationForm))
  }

  /**
    * userRegistration form.
    */
  val userRegistrationForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "cellphone" -> nonEmptyText,
      "country_code" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "useAuthy" -> nonEmptyText
    ) verifying ("Invalid entry", result => result match {
      case (username, cellphone, country_code, email, password, useAuthy) =>

        println("verifying userRegistrationForm with username: " + username)
//        User.findByUsername(username).exists
        true
    })
  )

  /**
    * POST userRegistration form.
    */
  def postUserRegistrationForm = Action { implicit request =>

    userRegistrationForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.userregistration(formWithErrors)),
      value => {
        val (username, cellphone, country_code, email, password, useAuthy) = userRegistrationForm.bindFromRequest.get
        println("Registering user: " + username + ", " + cellphone + ", " + country_code + ", " + email + ", <pw>, " + useAuthy)
        if (useAuthy == "true") {
          val authyData = AuthyUserRegistrationRequest(username, password, email, cellphone, country_code, useAuthy)
          val authyResponse = AuthyService.authyUserRegistration(authyData)
        }
        else {
          val authyUser = User(username, password, email, country_code, cellphone, "", "false")
          User.create(authyUser)
        }
        Redirect(routes.Authentication.login).withNewSession.flashing(
          "success" -> "You're registered!"
        )
      }
    )
//        username,
//        cellphone,
//        country_code
//      )
      //value => println(value.username)
      //value => println(username + cellphone)
      //value => println(username + cellphone + country_code)
      //user => Redirect(routes.Restricted.index()).withSession("username" -> user._1)
  //    user => Redirect(routes.Application.index()).withSession("username" -> user._1)
//    )
  //  try {
    //val authyUser = User("panderson", "foo", "panderson61@yahoo.com", "1", "7146146687", "38131306", "123")
    //val authyData = AuthyUserRegistrationRequest(authyUser.username, authyUser.phoneNumber, authyUser.countryCode)
    //val authyData = Json.fromJson[AuthyUserRegistrationRequest("u","p","c")](request.body.asJson.get).get


//    val authySomething = Json.toJson(authyResponse)
//    val authyId = (authySomething \ "authyId").get.toString()

    //Ok(Json.toJson(authyResponse))

  //  }
  //  catch {
  //    case e: Any => {
  //      exceptionHandler(e)
  //    }
  //  }

  //  userRegistrationForm.bindFromRequest.fold(
  //    formWithErrors => BadRequest(html.userregistration(formWithErrors)),
  //    user => Redirect(routes.Restricted.index()).withSession("email" -> user._2)
  //  )
  }

  private def exceptionHandler(exception: Any): Result = {

    Ok(views.html.index("unexpected error"))
    //Log exception
    //Exception.ignoring(classOf[Any]) {

      //exception match {
      //  case e: RuntimeException => requestLogger.error (e.getMessage)
      //}
    //}

    //exception match {
    //  case w: WsException => w.httpCode match {
    //    case 400 => BadRequest(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String](w.getMessage),Option[Int](w.picsCode.id))))
    //    case 404 => NotFound(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String](w.getMessage),Option[Int](w.picsCode.id))))
    //    case _ => InternalServerError(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String](w.getMessage),Option[Int](w.picsCode.id))))
    //  }
    //  case e: Exception => InternalServerError(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String](e.getMessage),Option[Int](ErrorCodes.generalError.id))))
    //  case _ => InternalServerError(Json.toJson(EmptyServiceResponse(Option[Boolean](false),Option[String]("Unknown exception."),Option[Int](ErrorCodes.generalError.id))))
    //}
  }
}