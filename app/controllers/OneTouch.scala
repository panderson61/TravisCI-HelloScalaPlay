package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import models.User
import models.authy._
import services.AuthyService

class OneTouch extends Controller {
  /**
    * Request Authentication approval from Users Authy app
    */
  def sendOneTouch = Action {
    // TODO get this data from model.User
    val myAuthyId = User.findByUsername("panderson").get.authyId
    val authyResponse = AuthyService.authySendOneTouch(AuthySendOneTouchRequest(myAuthyId, "authyservice login", "240"))
    // TODO put the expected response into model.User
    Ok(views.html.index(""))
  }

  /**
    * OneTouch CallBack page.
    */
  def callBack = Action { implicit request =>
    {
      println("OneTouch Call Back")
      println(s"    content-type: ${request.contentType}")
      println(s"    headers: ${request.headers}")
      println(s"    body: ${request.body}")
      println(s"    query string: ${request.rawQueryString}")
//      val myUser = User("","","","","","","")
      val teststr = AuthyService.authyCheckOneTouch( s"${request.body}" )
//      teststr match {
//        case "true" => Some(myUser)
//        case _ => None
//      }
      Ok(views.html.index(""))
    }
  }
}
