package controllers

import util.{HealthCheck, RequestLogging}
import scala.util.control.Exception
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.Play.current
import models.{HealthCheckResponse, HealthCheckLink}
import java.io.{FileInputStream, InputStream}
import play.api.cache.Cached

class Application extends Controller with RequestLogging {

  val logger = Logger(this.getClass())

  def index = RequestLoggingAction {
    response =>
      Ok("Hello World")
  }

  def version = RequestLoggingAction {
    //Try {
    try {

      import scala.collection.JavaConversions._

      val props = new java.util.Properties
      props.load(new FileInputStream(Play.application.path + "/conf/serviceVersion.properties"))
      val jsonVersion = "{" + props.stringPropertyNames.map(p => s""""${p}":"${props.getProperty(p)}"""").mkString(",") + "}"

      Ok(Json.parse(jsonVersion))
    }
    catch {
      // case e: Any => exceptionHandler(e)
      // case e: RuntimeException => requestLogger.error (e.getMessage)
      //val jsonVersion = "{" + "buildId:1, " + "commit:1" + "tag:v1.0.0" + "}"
      case e: Exception => InternalServerError(Json.parse("{" + "buildId:1, " + "commit:1" + "tag:v1.0.0" + "}"))
      //Failure(Json.parse(jsonVersion))
    }
  }

  def health = Cached.everything(_=> "health-check", play.Play.application.configuration.getInt("health.check.cache.duration")) {
    RequestLoggingAction {
      try {
        val healthCheckResponse =  HealthCheck.runHealthCheck

        requestLogger.debug(Json.toJson(healthCheckResponse).toString)

        if(healthCheckResponse.status.isDefined) {
          Ok(Json.toJson(healthCheckResponse))
        }
        else {
          InternalServerError(Json.toJson(healthCheckResponse))
        }
      }
      catch {
        case e: Exception => InternalServerError(Json.toJson(HealthCheckResponse(code = Option("Service.Failed"), message = Option("Unknown issue:" + e.getMessage), link = Option(HealthCheckLink(rel = "_self", href = "http://needsomethinghere.com")), status = None)))
      }
    }
  }

//  private def exceptionHandler(exception: Any): Result = {
//
    //Log exception
//    Exception.ignoring(classOf[Any]) {
//      exception match {
//        case e: RuntimeException => requestLogger.error (e.getMessage)
//      }
//    }
//  }
}
