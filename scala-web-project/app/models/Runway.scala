package models

import slick.driver.PostgresDriver.api._

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