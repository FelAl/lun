package controllers

import play.api._
import play.api.mvc._
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.joda.time.format.ISODateTimeFormat
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global
import models._

import scala.concurrent.{Await}
import scala.concurrent.duration._

class Application extends Controller {
  val countriesF = CountriesDAO.all
  val airportsF = AirportsDAO.all
  val runwaysF = RunwaysDAO.all
  val countries = Await.result(countriesF, 10.seconds)
  val airports = Await.result(airportsF, 10.seconds)
  val runways = Await.result(runwaysF, 10 seconds)


  val airportsPerCountryF = AirportsDAO.findByCountry("GE")
  val airportsPerCountry = Await.result(airportsPerCountryF, 10.seconds)
  println(airportsPerCountry)

  // val airportsPerCountry = AirportsDAO.findByCountry("EN")
  // println(airportsPerCountry)


  // val least = RunwaysDAO.least
  // println(least)
  def index = Action {
    // val temperature = 10

    Ok(views.html.index(countries, airports, runways))
  }

  def findWays = Action {
    Ok
  }

}