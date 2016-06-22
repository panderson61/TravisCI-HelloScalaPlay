package models.api

import play.api.libs.json.Json
import reactivemongo.bson.{BSONDocument, BSONString, BSONValue}
import util.{ErrorCodes, ServiceException}

case class Post( id: Option[String],
                 title: String,
                 body: String)

object Post {
  implicit val postFormat = Json.format[Post]

  def getMongoFilter(fieldName: String, filterValues: Vector[String], filterType: String): (String,BSONValue) = {
    filterType.toLowerCase match {
      case "in" => {
        fieldName match {
          case "title" => ("title", BSONDocument("$in" -> filterValues.map(BSONString)))
          case "body" => ("body", BSONDocument("$in" -> filterValues.map(BSONString)))
          case _ => throw new ServiceException("Cannot filter on Post." + fieldName, picsCode = ErrorCodes.InputJsonError, 400)
        }
      }
      case _ => throw new ServiceException("Unrecognized filter type on Post.", picsCode = ErrorCodes.InputJsonError, 400)
    }
  }
}