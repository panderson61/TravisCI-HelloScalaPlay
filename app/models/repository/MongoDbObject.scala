package models.repository

import reactivemongo.bson.BSONDocument

trait MongoDbObject {
  val id: String
  def dbCollection: String
  def toBSONDocument: BSONDocument
  def fromBSONDocument(bSONDocument: BSONDocument): MongoDbObject
}