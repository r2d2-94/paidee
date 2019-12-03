package paidee.restaurant.client
import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.dsl.Http4sClientDsl
import io.circe.generic.auto._
import org.http4s.dsl.io._
import io.circe.Json
import org.http4s.Uri.uri
import org.http4s.circe.CirceEntityEncoder._
import paidee.restaurant.models.TableRequest

import scala.concurrent.ExecutionContext.Implicits.global

object RestaurantClient  extends Http4sClientDsl[IO] {
  def run(args: List[String]): IO[ExitCode] = {
    val req = POST( Uri.uri("http://localhost:8080/v1/table/addItems"),TableRequest(1))
    val responseBody = BlazeClientBuilder[IO](global).resource.use(_.expect[String](req))
    responseBody.flatMap(resp => IO(println(resp))).as(ExitCode.Success)
  }
}