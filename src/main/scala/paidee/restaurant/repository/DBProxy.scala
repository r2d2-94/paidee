package paidee.restaurant.repository

import doobie._
import cats.effect.IO
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

object DBProxy {

  implicit val cs = IO.contextShift(ExecutionContext.global)
  private val config = ConfigFactory.load().getConfig("database")
  private val dbName =  config.getString("name")
  private val userName = config.getString("user")
  private val pass = config.getString("password")

  val db = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", s"jdbc:postgresql:$dbName", s"$userName", s"$pass"
  )
}
