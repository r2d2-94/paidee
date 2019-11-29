package paidee.restaurant.routes

import cats.effect.{IO, Sync}
import org.http4s._
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.dsl.Http4sDsl._
import org.http4s.circe.CirceEntityEncoder._
import paidee.restaurant.models.{TableItemsRequest, TableRequest}
import paidee.restaurant.service.{RestaurantService}

object RestaurantRoutes {

  def restaurantRoutes(R: RestaurantService): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO]{}
    import dsl._
    HttpRoutes.of[IO] {
      case req @ PUT  -> Root / "v1" / "table" / "addItems" =>
        req.decode[TableItemsRequest] { req =>
          for {
            serve <- R.addItem(req)
            res <- Ok(serve)
          } yield res
        }
      case req @ DELETE  -> Root / "v1" / "table" / "removeItem" =>
        req.decode[TableItemsRequest] { req =>
          for {
            serve <- R.deleteItem(req)
            resp <- Ok(serve)
          } yield resp
        }
      case req @ POST -> Root / "v1" / "table" / "orders" =>
        req.decode[TableRequest] { req =>
          for {
            serve <- R.queryItem(req)
            resp <- Ok(serve)
          } yield resp
        }
    }
  }
}
