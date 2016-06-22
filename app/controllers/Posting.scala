package controllers

import models.api.{Post, QueryFilter}
import play.api.libs.json.Json
import play.api.mvc.Action
import play.mvc.Controller
import services.PostingService
import util.{CustomErrorHandling, CustomParser}

class Posting extends Controller with CustomErrorHandling {

  def getPosts(filter: Option[String]) = Action {
    request =>

      try {
        val filters = if(filter.isDefined) CustomParser.jsonValidator[Vector[QueryFilter]](Json.parse(filter.get)) else Vector.empty[QueryFilter]
        val posts = PostingService.getPosts(filters)
        Ok(Json.toJson(posts))
      }
      catch {
        case e: Exception => apiExceptionHandler(e)
      }
  }

  def insertPost = Action {
    request =>

      try {
        val post = CustomParser.jsonValidator[Post](request.body.asJson.get)
        Console.println(post)
        val newPost = PostingService.insertPost(post)
        Created(Json.toJson(newPost))
      }
      catch {
        case e: Exception => apiExceptionHandler(e)
      }
  }

}
