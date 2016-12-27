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


import scala.reflect.runtime.universe._

class Application extends Controller {
  val countriesF = CountriesDAO.all
  val airportsF = AirportsDAO.all
  val runwaysF = RunwaysDAO.all
  val countries = Await.result(countriesF, 10.seconds)
  val airports = Await.result(airportsF, 10.seconds)
  val runways = Await.result(runwaysF, 10 seconds)

  // val airportsPerCountryF = AirportsDAO.findByCountry("GE")
  // val airportsPerCountry = Await.result(airportsPerCountryF, 10.seconds)
  // println(airportsPerCountry)

  // println("==========")
  // println("==========")
  // println("==========")
  // println("==========")

  // val runwaysPerAirportF = RunwaysDAO.findByAirport(20883)
  // val runwaysPerAirport = Await.result(runwaysPerAirportF, 10.seconds)
  // println(runwaysPerAirport)
  // val surfaces = runwaysPerAirport.flatMap(_.surface)
  // println(surfaces)
  // println("=====")
  // println(surfaces.distinct.length)
  // println("=====")



  // ReportGenerator.roadsInCountry("RU")
  // Works! 
  // val result = ReportGenerator.typeOfRunwaysPerCountry
  // println("========zzzzzzzzz========")
  // println(result)
  // val filteredRes=result.filter(x => !(x(1).toString == ""))
  // val reconClass = Recognizer.recognize(result)
  // println(reconClass)
  // Works!


  val airsPerCountry = ReportGenerator.sortByAirports
  val least = airsPerCountry.take(10)
  val most = airsPerCountry.takeRight(10).reverse

  println("most ====" + most)

  println("least ====" + least)


  // val least = RunwaysDAO.least
  // println(least)
  def index = Action {
    // val temperature = 10

    Ok(views.html.index(runways, Seq(Vector("RUS", "one <br> two <br>")), least, most))
  }

  def findWays = Action {
    Ok
  }

}