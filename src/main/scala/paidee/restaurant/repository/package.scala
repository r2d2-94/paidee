package paidee.restaurant
import doobie._
import doobie.implicits._
import cats.effect.IO
import scala.concurrent.ExecutionContext


package object repository {

  implicit val cs = IO.contextShift(ExecutionContext.global)

  val db = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql:postgres", "postgres", "postgres"
  )
  sql"""
    CREATE SCHEMA IF NOT EXISTS  myproduct
  """.update.run.transact(db).unsafeRunSync()
  sql"""
    CREATE TABLE IF NOT EXISTS  myproduct.orders(
    orderId SERIAL,
    tableId numeric,
    itemName text,
    timeToPrepare integer)
  """.update.run.transact(db).unsafeRunSync()
}

