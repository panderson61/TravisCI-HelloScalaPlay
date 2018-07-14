package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import models.User
import models.authy._
import services.AuthyService

class Token extends Controller {

  /**
    * Use SMS to send a token to the user's cellphone
    */
  def sendToken = Action {
    // TODO get this data from model.User
    val myAuthyId = User.findByUsername("panderson").get.authyId
    val authyResponse = AuthyService.authySendSMSToken(AuthySendSMSTokenRequest(myAuthyId))
    // TODO put the expected response into model.User
    Redirect(routes.Token.getTokenForm)
  }

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
      case (username, token) =>
        val myAuthyId = User.findByUsername(username).get.authyId
        val authyData = AuthyCheckSMSTokenRequest(myAuthyId, token)
        val authyResp = AuthyService.authyCheckSMSToken(authyData)
        authyResp.success.toString() == "true"
    })
  )
  /**
    * POST token form.
    */
  def postTokenForm = Action { implicit request =>
    tokenForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.token(formWithErrors)),
      user => {
        println("Token Session: " + user.toString())
        Redirect(routes.Restricted.index()).withSession("username" -> user._1)
      }
    )
  }
}