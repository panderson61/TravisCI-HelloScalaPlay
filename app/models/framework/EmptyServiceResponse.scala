package models.framework

case class EmptyServiceResponse (success: Option[Boolean],
                                 errorMsg: Option[String] = None,
                                 errorCode: Option[Int] = None)
  extends ServiceResponse

object EmptyServiceResponse {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val reads: Reads[EmptyServiceResponse] = (
    (__ \ "success").readNullable[Boolean] and
      (__ \ "errorMsg").readNullable[String] and
      (__ \ "errorCode").readNullable[Int]
    )(EmptyServiceResponse.apply _)

  implicit val writes: Writes[EmptyServiceResponse] = (
    (__ \ "success").writeNullable[Boolean] and
      (__ \ "errorMsg").writeNullable[String] and
      (__ \ "errorCode").writeNullable[Int]
    )(unlift(EmptyServiceResponse.unapply))

}