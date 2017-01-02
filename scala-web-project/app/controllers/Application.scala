package controllers

import play.api._
import play.api.mvc._
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.joda.time.format.ISODateTimeFormat
import play.api.Play.current
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global
import models._

import scala.concurrent.{Await}
import scala.concurrent.duration._

import scala.reflect.runtime.universe._
import scala.util.{Try => UTry, Success => USuccess, Failure => UFailure}

// caching
import play.api.cache._
import javax.inject.Inject

class Application @Inject() (cache: CacheApi) extends Controller {
  val airsPerCountry: Seq[(String, Int)] = cache.
    getOrElse[Seq[(String, Int)]]("airsPerCountry") {
      ReportGenerator.sortByAirports
    }
  val least = airsPerCountry.take(10)
  val most = airsPerCountry.takeRight(10).reverse

  val runwayTypes: Seq[Vector[AnyRef]] = cache.
    getOrElse[Seq[Vector[AnyRef]]]("runwayTypes") {
      ReportGenerator.typeOfRunwaysPerCountry
    } 

  val infoAboutCountries: Map[String,Seq[(String, Seq[Int])]] = cache. 
    getOrElse[Map[String,Seq[(String, Seq[Int])]]]("infoAboutCountries") {
      ReportGenerator.infoAboutCountries
    } 

  def index = Action {
    val info = Vector(("default",Vector(0)))

    Ok(views.html.index(runwayTypes, least, most, info, "None"))
  }

  def findInfo = Action { implicit request =>
    println(request)
    
    request.body.asFormUrlEncoded.map { request =>
      println(request.get("code"))
      val code = request.get("code").head

      println("***!!!****")
      println(code(0))
      // val info: Seq[(String, Vector[Int])] = UTry(ReportGenerator
      // val info = UTry(ReportGenerator
      //   .infoAboutCountry(code(0).toString))
      //   .getOrElse[Seq[(String, Seq[Int])]](Vector(("default",Vector(0))))

      val res = UTry(infoAboutCountries(code(0).toLowerCase))

      val info = res match {
        case USuccess(result) if (!result.isEmpty) => result
        case _ => Vector(("default",Vector(0)))
      }

      val resCode = code match {
        case c if (c.forall(_ == ' ')) => "None"
        case b if(!(b.isEmpty == true)) => b(0).toString
      }

      // println(info)
      // println("==")
      // println(resCode + "|")
      Ok(views.html.index(runwayTypes, least, most, info, resCode))
    } .getOrElse {
      BadRequest("Expecting request body")
    }   
  }
}
