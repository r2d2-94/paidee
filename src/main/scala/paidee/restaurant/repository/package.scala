package paidee.restaurant

import doobie._
import doobie.implicits._
import cats.effect.IO
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext


package object repository {

  implicit val cs = IO.contextShift(ExecutionContext.global)
  private val config = ConfigFactory.load().getConfig("database")
  private val dbName =  config.getString("name")
  private val userName = config.getString("user")
  private val pass = config.getString("password")

  val db = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", s"jdbc:postgresql:$dbName", s"$userName", s"$pass"
  )
  SQLQuery.initSchema.run.transact(db).unsafeRunSync()
  SQLQuery.initDB.run.transact(db).unsafeRunSync()
}

