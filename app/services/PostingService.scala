package services

import models.api.QueryFilter
import models.api.{Post => ApiPost}
import models.repository.{Post => DBPost}
import repositories.PostingServiceRepository
import reactivemongo.bson._
import util.ImplicitConversions._

object PostingService {

  def getPosts(queryFilters: Vector[QueryFilter]): Vector[ApiPost] = {
    //Build mongo BSON Doc filters from API filters
    val mongoFilter = BSONDocument(
      queryFilters.map(qf => ApiPost.getMongoFilter(qf.fieldName,qf.fieldValues,qf.filterType))
    )

    //Query mongo
    PostingServiceRepository.getObjects[DBPost](mongoFilter).map(DBPost.toApiPost)
  }

  def insertPost(apiPost: ApiPost): ApiPost = {

    //Check to make sure the calculation won't result in a circular reference

    //Get current post
    val currentPost = if(apiPost.id.isDefined) PostingServiceRepository.getObjects[DBPost](BSONDocument("id" -> stringToBSONObjectID(apiPost.id.get))).headOption else None

    //If the value hasn't changed, don't do any update

    //Create a db calculation from the values passed in the API
    val dBPost = DBPost(
      id = if(currentPost.isDefined) currentPost.get.id else BSONObjectID.generate,
      title = apiPost.title,
      body = apiPost.body
    )

    //Upsert updated post to database
    PostingServiceRepository.upsertMongoObject[DBPost](dBPost)
    //If the post exists create a log entry to store the current version

    //Get the affected calculation sub-graph

    //Recalculate each calculation node in the sub graph

    //Return the implicitly converted db -> api post object
    dBPost
  }
}
