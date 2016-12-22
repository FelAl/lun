package services

import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current
import models.{SunInfo}


class SunService {
  def getSunInfo(lat: Double, lon: Double) = {

  }
}
