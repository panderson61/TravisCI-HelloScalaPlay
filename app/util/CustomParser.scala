package util

import play.api.libs.json._


object CustomParser {
  def jsonValidator[T](inputjson: JsValue)(implicit fjs: Reads[T]) = {
    Json.fromJson[T](inputjson) match {
      case JsSuccess(parsedObject, _) => parsedObject
      case err@JsError(_) => throw new ServiceException(generateError(err), ErrorCodes.InputJsonError, 400)
    }
  }

  private def generateError(err: JsError) = {
    err.errors.map( x =>
      "Error with '" +
        x._1.toString().drop(1).filterNot(_.isDigit).replaceAll("\\(+\\)/", "/") +
        "'") .mkString("   &&   ")
  }
}
