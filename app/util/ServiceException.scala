package util

import util.ErrorCodes._

class ServiceException(msg: String,
                       val picsCode: ErrorCodes,
                       val httpCode: Int) extends RuntimeException(msg)