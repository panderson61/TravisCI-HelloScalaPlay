package controllers

import play.api.mvc.Controller
import models.User
import views._

class Restricted extends Controller with Secured {

  /**
    * Display restricted area only if user is logged in.
    */
  def index = IsAuthenticated { username =>
    _ =>
      User.findByUsername(username).map { user =>
        Ok(
          html.restricted(user)
        )
      }.getOrElse(Forbidden)
  }

}