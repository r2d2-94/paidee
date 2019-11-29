package paidee.restaurant.repository

import doobie.free.connection.ConnectionIO
import doobie.implicits._
import cats._
import cats.data._
import cats.effect._
import doobie._
import cats.implicits._
import com.typesafe.scalalogging.Logger
import doobie.util.query.Query0
import doobie.util.update.{Update, Update0}
import javafx.concurrent.Task
import paidee.restaurant.models.{Item, ItemDeleteResponse, ItemName, ResponseBase, TableId, TableResponse}
import paidee.restaurant.repository

import scala.concurrent.Future
import scala.util.control.NonFatal

sealed trait RestaurantRepository {

  def getItemsInTable(tableId : TableId) : IO[TableResponse]
  def deleteOrder(itemName : Seq[ItemName],tableId : TableId) : IO[ItemDeleteResponse]
  def addItemsToTable(itemName: Seq[ItemName],tableId: TableId) : IO[TableResponse]
}

 class RestaurantRepositoryImpl extends RestaurantRepository{
   val random =  scala.util.Random
   def r:Int = 1 + random.nextInt(14)
  val logger = Logger("RestaurantRepository")
  override def addItemsToTable(item: Seq[ItemName], tableId: TableId): IO[TableResponse] = {
    insertItems(item.map( x => RestaurantOrder(x,tableId,r)).toList).transact(repository.db).attempt.map{
      case Left(e) => logger.error("Unable to add items to table",e.getCause)
        ResponseBase.withError(Some("Sorry, your order could not be processed"))
      case Right(x) => TableResponse(items = item.iterator.map(x=> Item(x,r)).toSeq,success = true)
    }
  }

  override def deleteOrder(itemName: Seq[ItemName], tableId: TableId) : IO[ItemDeleteResponse] = {
    deleteOrder(itemName.map( x => DeleteOrder(x,tableId)).toList).transact(repository.db).attempt.map{
      case Left(e) => logger.error(s"Unable to delete items for table $tableId",e.getCause)
        ItemDeleteResponse(success = false,Some("Sorry, your order could not be processed"))
      case Right(_) => ItemDeleteResponse(success = true)
    }
  }

  override def getItemsInTable(tableId: TableId):  IO[TableResponse] = {
    queryItems(tableId).transact(repository.db).attempt.map{
        case Left(e) => logger.error(s"Unable to query items for table : $tableId",e.getCause)
          ResponseBase.withError(Some("Sorry, your order could not be processed"))
        case Right(x) => TableResponse(items = x, success = true)
      }
  }



  private def insertItems(order: List[RestaurantOrder]) : ConnectionIO[Int] = {
   val  sql = "insert into myproduct.orders (itemName, tableId, timeToPrepare) values(?, ? ,?) "
    Update[RestaurantOrder](sql).updateMany(order)
  }

  private def deleteOrder(order : List[DeleteOrder]) : ConnectionIO[Int] = {
    val  sql = "delete from myproduct.orders where itemName = ? and tableId = ? "
    Update[DeleteOrder](sql).updateMany(order)
  }

  private def queryItems(tableId : Int) : ConnectionIO[List[Item]] = {
    sql"select itemName, timeToPrepare from myproduct.orders where tableId = $tableId".query[Item].stream.compile.toList
  }
}

case class RestaurantOrder(itemName : ItemName, tableId : Int, timeToPrepare : Int)

case class DeleteOrder(itemName: ItemName, tableId: Int)

object RestaurantRepository {
  def apply(): RestaurantRepository = new RestaurantRepositoryImpl()
}

