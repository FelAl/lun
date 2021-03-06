package controllers

import play.api._
import play.api.mvc._
import models._
import database._
import services._

import scala.concurrent.{Await}
import scala.concurrent.duration._
import scala.util.{Try, Success, Failure}

import play.api.cache._
import javax.inject.Inject

class Application @Inject() (cache: CacheApi) extends Controller {
  val airsPerCountry: Seq[CountryRunways] = cache
    .getOrElse[Seq[CountryRunways]]("airsPerCountry") {
      ReportGenerator.sortByAirports
    }
  val least = airsPerCountry.take(10)
  val most = airsPerCountry.takeRight(10).reverse

  val runwayTypes: Seq[RunwayTypes] = cache
    .getOrElse[Seq[RunwayTypes]]("runwayTypes") {
      ReportGenerator.typeOfRunwaysPerCountry
    } 

  val infoAboutCountries: Map[String,CountryInfo] = cache
    .getOrElse[Map[String,CountryInfo]]("infoAboutCountries") {
      ReportGenerator.infoAboutCountries
    }

  val runwaysLeIdent = cache
    .getOrElse[Seq[RunwayLeIdent]]("runwaysLeIdent") {
      ReportDB.runwaysLeIdent
    }
  println(runwaysLeIdent)
  val countriesNone = CountryInfo(Vector(AirportInfo("default", Vector(0))))

  def index = Action {
    val countries = countriesNone

    Ok(views.html.index(runwayTypes, least, most, countries, "None", runwaysLeIdent))
  }

  def findInfo = Action { implicit request =>
    request.body.asFormUrlEncoded.map { request =>
      println(request.get("code"))
      val code = request.get("code").head

      println("Input: " + code(0))
      val clean_input = code(0).trim.replaceAll("\\s+"," ")
      val str_filtered = """[ ]*[a-zA-Z ]+$""".r findFirstIn clean_input
      val str_f = str_filtered match {
        case Some(str) if (str.forall(_ == ' ')) => "None"
        case Some(str) if (str.length > 0) => str
        case _ => "None"
      }
      println("After filtering: " + str_f)
      // Partial country name
      val partial_input = 
        if(str_f.length != 2) CMappingDB.returnFullName(str_f)
        else str_f
      // Full country name
      val fcode = partial_input match {
        case str if (str.length > 2) => Try(CMappingDB.nameToCodeMap(str.toLowerCase))
          .getOrElse("None")
        case str if (str.length == 2) => str
      }

      println("Actual code: " + fcode)
      val res = Try(infoAboutCountries(fcode.toLowerCase))

      // Just country code
      val countries = res match {
        case Success(result) if (!result.info.isEmpty) => result
        case _ => countriesNone
      }

      val resCode = str_f match {
        case c if (c.forall(_ == ' ')) => "None"
        case b if(!(b.isEmpty == true)) => b.toString
      }

      Ok(views.html.index(runwayTypes, least, most, countries, resCode, runwaysLeIdent))
    } .getOrElse {
      BadRequest("Expecting request body")
    }   
  }
}
