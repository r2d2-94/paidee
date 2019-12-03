package paidee.restaurant.repository

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import paidee.restaurant.models.TableRequest
import paidee.restaurant.service.RestaurantService
import circe.CirceEntityEncoder._

class RouteSpec extends org.specs2.mutable.Specification {

  "HelloWorld" >> {
    "return 200" >> {
      uriReturns200()
    }
    "return hello world" >> {
      uriReturnsHelloWorld()
    }
  }

  private[this] val restRaoutes: Response[IO] = {
    val getHW = Request[IO](Method.GET, uri"/v1/table/addItems")
    val service = RestaurantService.apply()
    paidee.restaurant.routes.RestaurantRoutes.restaurantRoutes(service).orNotFound(getHW).unsafeRunSync()
  }

  private[this] def uriReturns200(): MatchResult[Status] =
    restRaoutes.status must beEqualTo(Status.Ok)

  private[this] def uriReturnsHelloWorld(): MatchResult[String] =
    restRaoutes.as[TableRequest].unsafeRunSync() must beEqualTo("{\"message\":\"Hello, world\"}")
}