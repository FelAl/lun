package models

import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await}
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

object CountriesDAO extends TableQuery(new Countries(_)) {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val countries = TableQuery[Countries]

  def findById(id: Int): Future[Option[Country]] = {
    db.run(countries.filter(_.id === id).result).map(_.headOption)
  }

  def all: Future[Seq[Country]] = db.run(countries.result)

  def deleteById(id: Int): Future[Int] = {
    db.run(this.filter(_.id === id).delete)
  }
}

object AirportsDAO extends TableQuery(new Airports(_)) {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val airports = TableQuery[Airports]

  def findByCountry(iso_country: String) = {
    db.run(this.filter(_.iso_country === iso_country).result)
  }

  def findById(id: Int): Future[Option[Airport]] = {
    db.run(airports.filter(_.id === id).result).map(_.headOption)
  }

  def all: Future[Seq[Airport]] = db.run(airports.result)

  def deleteById(id: Int): Future[Int] = {
    db.run(this.filter(_.id === id).delete)
  }
}

object RunwaysDAO extends TableQuery(new Runways(_)) {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val runways = TableQuery[Runways]

  def findById(id: Int): Future[Option[Runway]] = {
    db.run(runways.filter(_.id === id).result).map(_.headOption)
  }

  def findByAirport(id_air: Int) = {
    db.run(this.filter(_.airport_ref === id_air).result)
  }

  def all: Future[Seq[Runway]] = db.run(runways.result)

  def deleteById(id: Int): Future[Int] = {
    db.run(this.filter(_.id === id).delete)
  }
}


object ReportGenerator {
  def typeOfRunwaysPerCountry: Seq[Vector[AnyRef]] = {
    val countriesF = CountriesDAO.all
    val countries = Await.result(countriesF, 10.seconds).take(4)

    val resultR = for {
      country <- countries 
      val roadsTypes = ReportGenerator.roadsInCountry(country.code)
      val roadsPerCounty = Vector(country.name, roadsTypes)

    } yield roadsPerCounty
    resultR
  }

  def roadsInCountry(country: String): String = {
    val airportsF = AirportsDAO.findByCountry(country)
    val airports = Await.result(airportsF, 10.seconds)
    val allRoads = for { 
      airport <- airports 
      val runwaysPerAirportF = RunwaysDAO.findByAirport(airport.id)
      val runwaysPerAirport = Await.result(runwaysPerAirportF, 10.seconds)
      runwaysFlat <- runwaysPerAirport
    } yield runwaysFlat
    val typeOfRoads = allRoads.flatMap(_.surface)
    println("typeOfRoads in " + country + " " + typeOfRoads)

    val distinctTypes = typeOfRoads.distinct
    val distinctToLC = distinctTypes.map(_.toLowerCase).distinct
    println("distinctType  in " + country + " " + distinctToLC)
    distinctToLC.mkString(" <br> ")
    // println(allRoads)
  }

  def sortByAirports = {
    val countriesF = CountriesDAO.all
    val countries = Await.result(countriesF, 10.seconds)
    val resultR = for {
      country <- countries
      val airportsNumber = ReportGenerator.airportsQuantity(country.code)
      val airportNumberPerCountry = (country.name, airportsNumber)
    } yield airportNumberPerCountry

    resultR.sortBy(_._2)
  }

  def airportsQuantity(country: String) = {
    val resF = AirportsDAO.findByCountry(country)
    val res = Await.result(resF, 1.seconds)
    // println(country + " : " + res.length)
    res.length
  }

  // def infoAboutCountries: Seq[(String, Seq[(String, Seq[Int])])] = {
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
    airPlusRoads
    val filtered = airPlusRoads.filter(x => !x._2.isEmpty)
    val result = filtered.map(airRoad => 
      (airRoad._1, airRoad._2.map (runway =>
        runway.id)))
    println("Processing: " + country) 
    result
  }
}
