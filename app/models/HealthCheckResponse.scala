package models

case class HealthCheckResponse(code: Option[String],
                               message: Option[String],
                               link: Option[HealthCheckLink],
                               status: Option[String]
                              )

object HealthCheckResponse {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val reads: Reads[HealthCheckResponse] = (
    (__ \ "code").readNullable[String] and
      (__ \ "message").readNullable[String] and
      (__ \ "link").readNullable[HealthCheckLink] and
      (__ \ "status").readNullable[String]
    )(HealthCheckResponse.apply _)

  implicit val writes: Writes[HealthCheckResponse] = (
    (__ \ "code").writeNullable[String] and
      (__ \ "message").writeNullable[String] and
      (__ \ "link").writeNullable[HealthCheckLink] and
      (__ \ "status").writeNullable[String]
    )(unlift(HealthCheckResponse.unapply))

}

case class HealthCheckLink(rel: String,
                           href: String)

object HealthCheckLink {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val reads: Reads[HealthCheckLink] = (
    (__ \ "rel").read[String] and
      (__ \ "href").read[String]
    )(HealthCheckLink.apply _)

  implicit val writes: Writes[HealthCheckLink] = (
    (__ \ "rel").write[String] and
      (__ \ "href").write[String]
    )(unlift(HealthCheckLink.unapply))

}