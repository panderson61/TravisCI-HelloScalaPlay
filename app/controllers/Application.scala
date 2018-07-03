package controllers

import javax.inject.Inject
import play.api.cache._
import play.api.mvc.Action
import play.api.mvc.Controller
import java.security.SecureRandom
import java.math.BigInteger


class Application @Inject() (cache: CacheApi) extends Controller {

  def index = Action {
    Ok(views.html.index(""))
  }
}