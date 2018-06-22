package util

import models.{HealthCheckResponse,HealthCheckLink}

object HealthCheck {

  def runHealthCheck: HealthCheckResponse = {

    HealthCheckResponse(code = None, message = None, link = None, status = Option("Ok"))
  }

}