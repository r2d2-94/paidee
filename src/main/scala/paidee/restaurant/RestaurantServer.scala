package paidee.restaurant

import cats.effect.{ContextShift, IO, Timer}
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import paidee.restaurant.routes.RestaurantRoutes
import paidee.restaurant.service.RestaurantService


object RestaurantServer {

  def stream(implicit T: Timer[IO], C: ContextShift[IO]): Stream[IO,Nothing] = {
    val service = RestaurantService.apply()
    val httpApp = RestaurantRoutes.restaurantRoutes(service).orNotFound
    val finalHttpApp = Logger.httpApp[IO](true, true)(httpApp)
    for {
      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}