package controllers

import play.api._
import play.api.mvc._
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.joda.time.format.ISODateTimeFormat
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global
import models.{SunInfo}
import services.{SunService}

class Application extends Controller {
  def index = Action {
    val temperature = 10
    Ok(views.html.index(temperature))
  }

  def findWays = Action {
    Ok
  }

}