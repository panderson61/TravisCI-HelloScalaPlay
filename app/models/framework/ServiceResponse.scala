package models.framework

abstract class ServiceResponse {
  def success: Option[Boolean]
  def errorMsg: Option[String]
  def errorCode: Option[Int]
}
