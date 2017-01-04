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
  val airsPerCountry: Seq[CountryRunways] = cache.
    getOrElse[Seq[CountryRunways]]("airsPerCountry") {
      ReportGenerator.sortByAirports
    }
  val least = airsPerCountry.take(10)
  val most = airsPerCountry.takeRight(10).reverse

  val runwayTypes: Seq[RunwayTypes] = cache.
    getOrElse[Seq[RunwayTypes]]("runwayTypes") {
      ReportGenerator.typeOfRunwaysPerCountry
    } 

  val infoAboutCountries: Map[String,CountryInfo] = cache. 
    getOrElse[Map[String,CountryInfo]]("infoAboutCountries") {
      ReportGenerator.infoAboutCountries
    } 

  def index = Action {
    val countries = CountryInfo(Vector(AirportInfo("default", Vector(0))))

    Ok(views.html.index(runwayTypes, least, most, countries, "None"))
  }

  def findInfo = Action { implicit request =>
    request.body.asFormUrlEncoded.map { request =>
      println(request.get("code"))
      val code = request.get("code").head

      println("Input: " + code(0))

      val fcode = code(0) match {
        case str if (str.length > 2) => Try(CMapping.nameToCode(str)).getOrElse("None")
        case str if (str.length == 2) => str
      }

      val res = Try(infoAboutCountries(fcode.toLowerCase))

      val countries = res match {
        case Success(result) if (!result.info.isEmpty) => result
        case _ => CountryInfo(Vector(AirportInfo("default",Vector(0))))
      }

      val resCode = code match {
        case c if (c.forall(_ == ' ')) => "None"
        case b if(!(b.isEmpty == true)) => b(0).toString
      }

      Ok(views.html.index(runwayTypes, least, most, countries, resCode))
    } .getOrElse {
      BadRequest("Expecting request body")
    }   
  }
}
