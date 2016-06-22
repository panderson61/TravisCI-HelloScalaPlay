package models.api

import play.api.libs.json.Json

case class QueryFilter(fieldName: String,
                       fieldValues: Vector[String],
                       filterType: String)

object QueryFilter {
  implicit val queryFilterFormat = Json.format[QueryFilter]
}