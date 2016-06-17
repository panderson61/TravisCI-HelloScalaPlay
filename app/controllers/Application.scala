package controllers

import play.api._
import play.api.mvc._


class Application extends Controller {

  def index = Action {
    Ok("Hello World")
  }

  def version = Action {
    val version = Application.getClass.getPackage.getImplementationVersion
    Ok(version)
  }
}
