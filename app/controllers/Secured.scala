package controllers

import play.api.mvc._

/**
  * Provide security features
  */
trait Secured {

  /**
    * Retrieve the connected user's email
    */
//  private def username(request: RequestHeader) = request.session.get("email")
  private def mySessionToken(request: RequestHeader) = request.session.get("username")

  /**
    * Not authorized, forward to login
    */
  private def onUnauthorized(request: RequestHeader) = {
    println("Secured UnAuthorized access!")
    Results.Redirect(routes.Authentication.login)
  }

  /**
    * Action for authenticated users.
    */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = {
    println("Secured IsAuthenticated")
    Security.Authenticated(mySessionToken, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
}