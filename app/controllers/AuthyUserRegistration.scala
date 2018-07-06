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
      "username" -> text,
      "cellphone" -> text,
      "country_code" -> text
    ) verifying ("Invalid entry", result => result match {
      case (username, cellphone, country_code) => User.matchCC(country_code).isDefined
    })
  )

  /**
    * POST userRegistration form.
    */
  def postUserRegistrationForm = Action { implicit request =>

  //  userRegistrationForm.bindFromRequest.fold(
  //    formWithErrors => BadRequest(html.userregistration(formWithErrors)),
      //user => Redirect(routes.Restricted.index()).withSession("username" -> user._1)
  //    user => Redirect(routes.Application.index()).withSession("username" -> user._1)
  //  )
  //  try {
      val authyData = AuthyUserRegistrationRequest("u","p","c")
      //val authyData = Json.fromJson[AuthyUserRegistrationRequest("u","p","c")](request.body.asJson.get).get
      val authyResponse = AuthyService.authyUserRegistration(authyData)
      Ok(Json.toJson(authyResponse))
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