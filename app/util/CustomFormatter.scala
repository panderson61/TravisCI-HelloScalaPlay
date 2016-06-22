package util

import org.joda.time.format.ISODateTimeFormat

object CustomFormatter {
  def isoTimeToMills(isoTime: String): Long = {
    ISODateTimeFormat.dateTime.parseDateTime(isoTime).getMillis
  }
}