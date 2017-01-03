package services

import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await}
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

import database._

object ReportGenerator {
  def typeOfRunwaysPerCountry: Seq[Vector[AnyRef]] = {
    val countriesF = CountriesDAO.all
    val countries = Await.result(countriesF, 10.seconds).take(4)

    val resultR = for {
      country <- countries 
      roadsTypes = ReportGenerator.roadsInCountry(country.code)
      roadsPerCounty = Vector(country.name, roadsTypes)

    } yield roadsPerCounty
    resultR
  }

  def roadsInCountry(country: String): String = {
    val airportsF = AirportsDAO.findByCountry(country)
    val airports = Await.result(airportsF, 10.seconds)
    val allRoads = for { 
      airport <- airports 
      runwaysPerAirportF = RunwaysDAO.findByAirport(airport.id)
      runwaysPerAirport = Await.result(runwaysPerAirportF, 10.seconds)
      runwaysFlat <- runwaysPerAirport
    } yield runwaysFlat
    val typeOfRoads = allRoads.flatMap(_.surface)
    val distinctTypes = typeOfRoads.distinct
    val distinctToLC = distinctTypes.map(_.toLowerCase).distinct
    println("distinctType  in " + country + " " + distinctToLC)
    distinctToLC.mkString(" <br> ")
  }

  def sortByAirports = {
    val countriesF = CountriesDAO.all
    val countries = Await.result(countriesF, 10.seconds)
    val resultR = for {
      country <- countries
      airportsNumber = ReportGenerator.airportsQuantity(country.code)
      airportNumberPerCountry = (country.name, airportsNumber)
    } yield airportNumberPerCountry

    resultR.sortBy(_._2)
  }

  def airportsQuantity(country: String) = {
    val resF = AirportsDAO.findByCountry(country)
    val res = Await.result(resF, 1.seconds)
    res.length
  }

  def infoAboutCountries: Map[String,Seq[(String, Seq[Int])]] = {
    val countriesF = CountriesDAO.all
    val countries = Await.result(countriesF, 10.seconds).take(4)
    val result = for {
      country <- countries

      info = ReportGenerator.infoAboutCountry(country.code)
    } yield (country.code.toLowerCase, info)
    result.toMap
  }

  def infoAboutCountry(country: String) = {
    val airportsF = AirportsDAO.findByCountry(country)
    val airports = Await.result(airportsF, 10.seconds)

    val airPlusRoads = for {
      airport <- airports
      runwaysPerAirportF = RunwaysDAO.findByAirport(airport.id)
      runwaysPerAirport = Await.result(runwaysPerAirportF, 10.seconds)
    } yield (airport.name, runwaysPerAirport)
    val filtered = airPlusRoads.filter(x => !x._2.isEmpty)
    val result = filtered.map(airRoad => 
      (airRoad._1, airRoad._2.map (runway =>
        runway.id)))
    println("Processing: " + country) 
    result
  }
}
