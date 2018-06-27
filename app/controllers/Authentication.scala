package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import models.User

class Authentication extends Controller {

  val login = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying ("Invalid username or password", result => result match {
      case (username, password) => User.authenticate(username, password).isDefined
    })
  )

  /**
    * Login page.
    */
  def loginForm = Action { implicit request =>
    Ok(html.login(login))
  }

  /**
    * Logout and clean the session.
    */
  def logout = Action {
    Redirect(routes.Authentication.loginForm).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  /**
    * Handle login form submission.
    */
  def authenticate = Action {
    Ok("authenticated")
  }
  //def authenticate = Action { implicit request =>
  //  loginForm.bindFromRequest.fold(
  //    formWithErrors => BadRequest(html.login(formWithErrors)),
  //    user => Redirect(routes.Restricted.index()).withSession("email" -> user._1)
  //  )
  //}

}