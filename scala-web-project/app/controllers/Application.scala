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

class Application extends Controller {
  val countriesF = CountriesDAO.all
  val airportsF = AirportsDAO.all
  val runwaysF = RunwaysDAO.all
  val countries = Await.result(countriesF, 10.seconds)
  val airports = Await.result(airportsF, 10.seconds)
  val runways = Await.result(runwaysF, 10 seconds)

  val airsPerCountry = ReportGenerator.sortByAirports
  val least = airsPerCountry.take(10)
  val most = airsPerCountry.takeRight(10).reverse
  val runwayTypes = ReportGenerator.typeOfRunwaysPerCountry


  println("on start only!!!!!")
  println("on start only!!!!!")
  println("on start only!!!!!")
  println("on start only!!!!!")
  println("on start only!!!!!")
  println("on start only!!!!!")
  println("on start only!!!!!")
  println("on start only!!!!!")
  def index = Action {
    // val temperature = 10
    val info: Seq[(String, Seq[Int])] = ReportGenerator.infoAboutCountry("RU")

    Ok(views.html.index(runways, runwayTypes, least, most, info))
  }

  def findInfo = Action { implicit request =>
    println(request)
    

    request.body.asFormUrlEncoded.map { request =>
       println(request.get("code"))
       val code = request.get("code").head
       println("***!!!****")
       println(code(0))  
       val info: Seq[(String, Seq[Int])] = ReportGenerator.infoAboutCountry(code(0).toString)
       println(info)
       Ok(views.html.index(runways, Seq(Vector("RUS", "one <br> two <br>")), least, most, info))
    } .getOrElse {
    BadRequest("Expecting application/json request body")
  }
    // println(request.body.get("code"))
   
  }

}