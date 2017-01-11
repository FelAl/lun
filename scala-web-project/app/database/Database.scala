package database

import models._
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await}
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory

case class RunwayLeIdent(le_ident: String, quantity: Int)

object CountriesDAO {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  val countries = TableQuery[Countries]
  val airports = TableQuery[Airports]
  val runways = TableQuery[Runways]

  def findById(id: Int): Future[Option[Country]] = {
    val q = countries.filter(_.id === id)
    val action = q.result
    val result = db.run(action)
    val finalResult = result.map(_.headOption)
    finalResult
  }

  def findInfo(code: String) = {

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

object CMappingDB {
  val countriesF = CountriesDAO.all
  val countries = Await.result(countriesF, 1.seconds)
  val nameToCodeSeq = countries.map(c => c.name.toLowerCase -> c.code)
  val nameToCodeMap = nameToCodeSeq.toMap

  def returnFullName(str: String) = {
    println("fullName method + params: " + str)
    val keys = nameToCodeMap.keys
    val filtered_keys = for 
      {key <- keys 
        if ((key.length >= str.length) &&
          (key.substring(0, str.length).toLowerCase == str.toLowerCase))}
      yield key
    println("filtered keys:  " + filtered_keys)
    val result = filtered_keys match {
      case x if (filtered_keys.size == 1) => x
      case _ => Set("None")
    }
    println("result fullName: " + result)
    val res = result.toSeq(0)
    res
  }
}

object ReportDB {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("databaseUrl")

  def runwaysLeIdent: Seq[RunwayLeIdent] = {
    val q = sql"""SELECT le_ident, count(*) from "RUNWAYS" 
      group by "le_ident" order by count(*) desc LIMIT 10;""".as[(String, Int)]
    val resF = db.run(q)
    val res = Await.result(resF, 1.seconds).map(x => RunwayLeIdent(x._1, x._2))
    res
  }
}