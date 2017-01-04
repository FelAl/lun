package services

import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await}
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

import database._

case class AirportInfo(name: String, runways: Seq[Int])
case class CountryInfo(info: Seq[AirportInfo])

case class CountryRunways(country: String, quantity: Int)
case class CountryRunwaysList(list: Seq[CountryRunways])

case class RunwayTypes(country: String, types: String)

object ReportGenerator {
  def typeOfRunwaysPerCountry: Seq[RunwayTypes] = {
    val countriesF = CountriesDAO.all
    val countries = Await.result(countriesF, 10.seconds).take(4)

    val resultR = for {
      country <- countries 
      roadsTypes = ReportGenerator.roadsInCountry(country.code)
      roadsPerCounty = RunwayTypes(country.name, roadsTypes)

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

  def sortByAirports: Seq[CountryRunways] = {
    val countriesF = CountriesDAO.all
    val countries = Await.result(countriesF, 10.seconds)
    val result = for {
      country <- countries
      airportsNumber = ReportGenerator.airportsQuantity(country.code)
      airportNumberPerCountry = CountryRunways(country.name, airportsNumber)
    } yield airportNumberPerCountry

    result.sortBy(_.quantity)
  }

  def airportsQuantity(country: String) = {
    val resF = AirportsDAO.findByCountry(country)
    val res = Await.result(resF, 1.seconds)
    res.length
  }

  def infoAboutCountries: Map[String, CountryInfo] = {
    val countriesF = CountriesDAO.all
    val countries = Await.result(countriesF, 10.seconds).take(4)
    val result = for {
      country <- countries

      info = ReportGenerator.infoAboutCountry(country.code)
    } yield (country.code.toLowerCase, info)
    result.toMap
  }

  def infoAboutCountry(country: String): CountryInfo = {
    val airportsF = AirportsDAO.findByCountry(country)
    val airports = Await.result(airportsF, 10.seconds)

    val airPlusRoads = for {
      airport <- airports
      runwaysPerAirportF = RunwaysDAO.findByAirport(airport.id)
      runwaysPerAirport = Await.result(runwaysPerAirportF, 10.seconds)
      runwaysIds = runwaysPerAirport.map(runway => runway.id)
    } yield AirportInfo(airport.name, runwaysIds)
    val filtered = airPlusRoads.filter(x => !x.runways.isEmpty)
    println("Processing: " + country) 
    val result = CountryInfo(filtered)
    result
  }
}
