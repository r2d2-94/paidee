package paidee.restaurant

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    RestaurantServer.stream.compile.drain.as(ExitCode.Success)
}