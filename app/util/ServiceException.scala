package util

import ErrorCodes._

class ServiceException(msg: String,
                       val picsCode: ErrorCodes,
                       val serviceName: String,
                       val httpCode: Int) extends RuntimeException(msg)