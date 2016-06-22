package util

import org.joda.time.format.ISODateTimeFormat
import util.CustomFormatter.isoTimeToMills
import reactivemongo.bson.{BSONDateTime, BSONObjectID}
import org.joda.time.DateTime

object ImplicitConversions {

  private def bytesToHex(bytes : Array[Byte]) = bytes.map{ b => String.format("%02x", java.lang.Byte.valueOf(b)) }.mkString

  implicit def bsonObjectIdToString(bSONObjectID: BSONObjectID): String = bytesToHex(bSONObjectID.valueAsArray)
  implicit def stringToBSONObjectID(objectId: String): BSONObjectID = BSONObjectID.apply(objectId)
  implicit def bsonTimestampToDateTime(bSONDateTime: BSONDateTime): DateTime = new DateTime(bSONDateTime.value)
  implicit def dateTimeToBSONDateTime(dateTime: DateTime): BSONDateTime = new BSONDateTime(dateTime.getMillis)
  implicit def bsonOptionTimestampToDateTime(bSONDateTime: Option[BSONDateTime]): Option[DateTime] = if(bSONDateTime.isDefined) Option(new DateTime(bSONDateTime.get.value)) else None
  implicit def dateOptionTimeToBSONDateTime(dateTime: Option[DateTime]): Option[BSONDateTime] = if(dateTime.isDefined) Option(new BSONDateTime(dateTime.get.getMillis)) else None
  //ISO 8601 DateTime serializers
  implicit def dateTimeToISO8601String(dateTime: DateTime): String = ISODateTimeFormat.dateTime.print(dateTime)
  implicit def dateTimeOptionToISO8601String(dateTime: Option[DateTime]): Option[String] = if(dateTime.isDefined) Option(ISODateTimeFormat.dateTime.print(dateTime.get)) else None
  implicit def iSO8601StringToDateTime(iso8601String: String): DateTime = new DateTime(isoTimeToMills(iso8601String))
  implicit def iSO8601StringOptionToDateTime(iso8601String: Option[String]): Option[DateTime] = if(iso8601String.isDefined) Option(new DateTime(isoTimeToMills(iso8601String.get))) else None
}