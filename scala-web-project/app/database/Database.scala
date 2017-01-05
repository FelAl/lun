package database

import models._
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await}
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

object CountriesDAO {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val countries = TableQuery[Countries]

  def findById(id: Int): Future[Option[Country]] = {
    val q = countries.filter(_.id === id)
    val action = q.result
    val result = db.run(action)
    val finalResult = result.map(_.headOption)
    finalResult
  }

  def all: Future[Seq[Country]] = db.run(countries.result)
}

object AirportsDAO {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val airports = TableQuery[Airports]

  def findByCountry(iso_country: String): Future[Seq[Airport]] = {
    val q = airports.filter(_.iso_country === iso_country)
    val action = q.result
    val result = db.run(action)
    result
  }

  def findById(id: Int): Future[Option[Airport]] = {
    val q = airports.filter(_.id === id)
    val action = q.result
    val result = db.run(action)
    val finalResult = result.map(_.headOption)
    finalResult
  }

  def all: Future[Seq[Airport]] = db.run(airports.result)
}

object RunwaysDAO {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val runways = TableQuery[Runways]

  def findById(id: Int): Future[Option[Runway]] = {
    val q = runways.filter(_.id === id)
    val action = q.result
    val result = db.run(action)
    val finalResult = result.map(_.headOption)
    finalResult

  }

  def findByAirport(id_air: Int) = {
    val q = runways.filter(_.airport_ref === id_air)
    val action = q.result
    val result = db.run(action)
    result
  }

  def all: Future[Seq[Runway]] = db.run(runways.result)
}
