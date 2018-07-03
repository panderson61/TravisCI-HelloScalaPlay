package models.framework

import util.ErrorCodes.ErrorCodes

class WsException(msg: String,
                  val picsCode: ErrorCodes,
                  val httpCode: Int) extends RuntimeException(msg)
