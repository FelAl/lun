package database

import models._
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

  def findByCountry(iso_country: String): Future[Seq[Airport]] = {
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
