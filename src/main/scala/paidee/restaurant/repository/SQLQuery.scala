package paidee.restaurant.repository

import doobie.free.connection.ConnectionIO
import doobie.util.update.Update
import paidee.restaurant.models.{Item, ItemName}
import doobie.implicits._
import cats.implicits._
import com.typesafe.config.ConfigFactory
object SQLQuery {

  val initSchema = sql"""
    CREATE SCHEMA IF NOT EXISTS myproduct
  """.update

  val initDB =   sql"""
    CREATE TABLE IF NOT EXISTS myproduct.orders(
    orderId SERIAL,
    tableId numeric,
    itemName text,
    timeToPrepare integer)
  """.update

   def insertItems(order: List[RestaurantOrder]) : ConnectionIO[Int] = {
    val  sql = "insert into myproduct.orders (itemName, tableId, timeToPrepare) values(?, ? ,?) "
    Update[RestaurantOrder](sql).updateMany(order)
  }

   def deleteOrder(order : List[DeleteOrder]) : ConnectionIO[Int] = {
    val  sql = "delete from myproduct.orders where itemName = ? and tableId = ? "
    Update[DeleteOrder](sql).updateMany(order)
  }

   def queryItems(tableId : Int) : ConnectionIO[List[Item]] = {
    sql"select itemName, timeToPrepare from myproduct.orders where tableId = $tableId".query[Item].stream.compile.toList
  }

}