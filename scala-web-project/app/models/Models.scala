package models

import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await}
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

case class Country(id: Int, code: Option[String], name: Option[String], continent: Option[String],
  wikipedia_link: Option[String], keywords: Option[String])

class Countries (tag: Tag) extends Table[Country](tag, "COUNTRIES") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def code = column[Option[String]]("code")
  def name = column[Option[String]]("name")
  def continent = column[Option[String]]("continent")
  def wikipedia_link = column[Option[String]]("wikipedia_link")
  def keywords = column[Option[String]]("keywords")

  def * = (id, code, name, continent, 
    wikipedia_link, keywords) <> (Country.tupled, Country.unapply)
}

object CountriesDAO extends TableQuery(new Countries(_)) {
  // println(Database.getClass) -> slick.jdbc.JdbcBackend$$anon$3
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

case class Airport(id: Int, ident: String, typ: String, name: String,
  latitude_deg: Option[Double], longitude_deg: Option[Double], elevation_ft: Option[Int],
  continent: Option[String], iso_country: Option[String], iso_region: Option[String],
  municipality: Option[String], scheduled_service: Option[String], gps_code: Option[String],
  iata_code: Option[String], local_code: Option[String], home_link: Option[String],
  wikipedia_link: Option[String], keywords: Option[String])

class Airports (tag: Tag) extends Table[Airport](tag, "AIRPORTS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def ident = column[String]("ident")
  def typ = column[String]("typ")
  def name = column[String]("name")
  def latitude_deg = column[Option[Double]]("latitude_deg")
  def longitude_deg = column[Option[Double]]("longitude_deg")
  def elevation_ft = column[Option[Int]]("elevation_ft")
  def continent = column[Option[String]]("continent")
  def iso_country = column[Option[String]]("iso_country")
  def iso_region = column[Option[String]]("iso_region")
  def municipality = column[Option[String]]("municipality")
  def scheduled_service = column[Option[String]]("scheduled_service")
  def gps_code = column[Option[String]]("gps_code")
  def iata_code = column[Option[String]]("iata_code")
  def local_code = column[Option[String]]("local_code")
  def home_link = column[Option[String]]("home_link")
  def wikipedia_link = column[Option[String]]("wikipedia_link")
  def keywords = column[Option[String]]("keywords")

  def * = (id, ident, typ, name, latitude_deg, longitude_deg, elevation_ft, continent,
    iso_country, iso_region, municipality, scheduled_service, gps_code, iata_code, local_code, home_link,
    wikipedia_link, keywords) <> (Airport.tupled, Airport.unapply)
}

object AirportsDAO extends TableQuery(new Airports(_)) {
  // println(Database.getClass) -> slick.jdbc.JdbcBackend$$anon$3
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val airports = TableQuery[Airports]

  // val addressesQuery = airports.flatMap(_.iso_country)
  // val iso_countries = db.run(addressesQuery.result)
  // println(iso_countries)

  // def findByCountry(iso_country: String): Future[Option[Seq[Airport]]] = {
  //   db.run((airports.map(_.iso_country === iso_country)).result)
  // }

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

case class Runway(id: Int, airport_ref: Int, airport_ident: String,
  length_ft: Option[Int], width_ft: Option[Int], surface: Option[String], lighted: Option[Short],
  closed: Option[Short], le_ident: Option[String], le_latitude_deg: Option[Double], 
  le_longitude_deg: Option[Double], le_elevation_ft: Option[Int], le_heading_degT: Option[Int],
  le_displaced_threshold_ft: Option[Int], he_ident: Option[String], he_latitude_deg: Option[Double],
  he_longitude_deg: Option[Double], he_elevation_ft: Option[Int], he_heading_degT: Option[Int],
  he_displaced_threshold_ft: Option[Int])

class Runways (tag: Tag) extends Table[Runway](tag, "RUNWAYS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def airport_ref = column[Int]("airport_ref")
  def airport_ident = column[String]("airport_ident")
  def length_ft = column[Option[Int]]("length_ft")
  def width_ft = column[Option[Int]]("width_ft")
  def surface = column[Option[String]]("surface")
  def lighted = column[Option[Short]]("lighted")
  def closed = column[Option[Short]]("closed")
  def le_ident = column[Option[String]]("le_ident")
  def le_latitude_deg = column[Option[Double]]("le_latitude_deg")
  def le_longitude_deg = column[Option[Double]]("le_longitude_deg")
  def le_elevation_ft = column[Option[Int]]("le_elevation_ft")
  def le_heading_degT = column[Option[Int]]("le_heading_degT")
  def le_displaced_threshold_ft = column[Option[Int]]("le_displaced_threshold_ft")
  def he_ident = column[Option[String]]("he_ident")
  def he_latitude_deg = column[Option[Double]]("he_latitude_deg")
  def he_longitude_deg = column[Option[Double]]("he_longitude_deg")
  def he_elevation_ft = column[Option[Int]]("he_elevation_ft")
  def he_heading_degT = column[Option[Int]]("he_heading_degT")
  def he_displaced_threshold_ft = column[Option[Int]]("he_displaced_threshold_ft")

  def * = (id, airport_ref, airport_ident, length_ft, width_ft, surface, lighted, closed,
    le_ident, le_latitude_deg, le_longitude_deg, le_elevation_ft, le_heading_degT, 
    le_displaced_threshold_ft, he_ident, he_latitude_deg, he_longitude_deg, 
    he_elevation_ft, he_heading_degT, he_displaced_threshold_ft) <> (Runway.tupled, Runway.unapply)
}

object RunwaysDAO extends TableQuery(new Runways(_)) {
  // println(Database.getClass) -> slick.jdbc.JdbcBackend$$anon$3
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val runways = TableQuery[Runways]

  def findById(id: Int): Future[Option[Runway]] = {
    db.run(runways.filter(_.id === id).result).map(_.headOption)
  }

  // val leastVar = runways.sortBy(_.le_ident.desc).take(10)

  // def least: Future[Seq[Runway]] = leastVar

  def all: Future[Seq[Runway]] = db.run(runways.result)

  def deleteById(id: Int): Future[Int] = {
    db.run(this.filter(_.id === id).delete)
  }
}