package models.repository

import models.api.{Post => ApiPost}
import reactivemongo.bson._
import util.ImplicitConversions._


case class Post ( id: String,
                  title: String,
                  body: String) extends MongoDbObject {

  def dbCollection = "post"
  def toBSONDocument = Post.PostWriter.write(this)
  def fromBSONDocument(doc: BSONDocument) = Post.PostReader.read(doc: BSONDocument)
}

object Post {

  implicit def toApiPost(post: Post): ApiPost = {
    ApiPost(
      id = Option(post.id),
      title = post.title,
      body = post.body
    )
  }

  implicit object PostReader extends BSONDocumentReader[Post] {
    def read(doc: BSONDocument): Post = {
      Post(
        doc.getAs[BSONObjectID]("id").get,
        doc.getAs[String]("title").get,
        doc.getAs[String]("body").get
      )
    }
  }

  implicit object PostWriter extends BSONDocumentWriter[Post] {
    def write(post: Post): BSONDocument = BSONDocument(
      "id" -> stringToBSONObjectID(post.id),
      "title" -> post.title,
      "body" -> post.body
    )
  }
}