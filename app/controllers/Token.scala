package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import models.User

class Token extends Controller {

  /**
    * GET token form.
    */
  def getTokenForm = Action { implicit request =>
    Ok(html.token(tokenForm))
  }

  /**
    * token form.
    */
  val tokenForm = Form(
    tuple(
      "username" -> text,
      "token" -> text
    ) verifying ("Invalid username or token", result => result match {
      case (username, token) => User.verifyToken(username, token).isDefined
    })
  )
  /**
    * POST token form.
    */
  def postTokenForm = Action { implicit request =>
    tokenForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.token(formWithErrors)),
      user => Redirect(routes.Restricted.index()).withSession("email" -> user._2)
    )
  }
}