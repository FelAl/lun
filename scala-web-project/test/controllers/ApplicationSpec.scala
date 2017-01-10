package controllers

// import scala.concurrent.Future

import org.scalatestplus.play._

import play.api.Play.current
import play.api.libs.ws._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

class ApplicationSpec extends PlaySpec with Results {

  "Application" should {
    "be reachable" in {
      running(FakeApplication()) {
        val response = await(WS.url("http://localhost:9000").get())
        response.status mustBe (OK)
      }
    }
  }
}
