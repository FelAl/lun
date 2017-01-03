package models

import slick.driver.PostgresDriver.api._

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
    iso_country, iso_region, municipality, scheduled_service, gps_code, iata_code, local_code,
    home_link, wikipedia_link, keywords) <> (Airport.tupled, Airport.unapply)
}