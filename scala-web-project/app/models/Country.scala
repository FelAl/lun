package models

import slick.driver.PostgresDriver.api._

case class Country(id: Int, code: String, name: String, continent: Option[String],
  wikipedia_link: Option[String], keywords: Option[String])

class Countries (tag: Tag) extends Table[Country](tag, "COUNTRIES") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def code = column[String]("code")
  def name = column[String]("name")
  def continent = column[Option[String]]("continent")
  def wikipedia_link = column[Option[String]]("wikipedia_link")
  def keywords = column[Option[String]]("keywords")

  def * = (id, code, name, continent, 
    wikipedia_link, keywords) <> (Country.tupled, Country.unapply)
}