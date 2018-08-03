package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import models.User

class Authentication extends Controller {

  //val login = Form(
  //  mapping(
  //    "username" -> text,
  //    "password" -> text,
  //    "email"    -> text
  //  )(User.apply)(User.unapply)
  //)
  val loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying ("Invalid username or password", result => result match {
      case (username, password) => User.authenticate(username, password).isDefined
    })
  )

  /**
    * Login page.
    */
  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  /**
    * Logout and clean the session.
    */
  def logout = Action {
    Redirect(routes.Authentication.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  /**
    * Handle login form submission.
    */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => {
        val mySessionToken = user._1
        println("Login session: " + user.toString() + "and token: " + mySessionToken)
        val myOptionUser = User.findByUsername(mySessionToken.toString)
        val myUser = myOptionUser.getOrElse(User("","","","","","",""))
        val myUseAuthy = myUser.useAuthy.toString
        println("UseAuthy is: " + myUseAuthy)
        val myNext = myUseAuthy match {
          case "smstoken" => routes.Token.sendToken()
          case "false" => routes.Restricted.index()
          case _ => routes.Authentication.logout()
        }
        Redirect(myNext).withSession("username" -> mySessionToken)
      }
    )
  }

//  def send2FApprovalRequest(user: User) : Boolean = {true}

//  def sendOneTouchApprovalRequest(user: User) : Boolean = {true}

//  def sendSmsTokenRequest(user: User) : Boolean = {true}
}