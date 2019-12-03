package paidee.restaurant.repository

import cats.effect._
import doobie.implicits._
import com.typesafe.scalalogging.Logger
import doobie.util.transactor.Transactor
import paidee.restaurant.models.{Item, ItemDeleteResponse, ItemName, ResponseBase, TableId, TableResponse}
import paidee.restaurant.repository

sealed trait RestaurantRepository {

  def getItemsInTable(tableId : TableId) : IO[TableResponse]
  def deleteOrder(itemName : Seq[ItemName],tableId : TableId) : IO[ItemDeleteResponse]
  def addItemsToTable(itemName: Seq[ItemName],tableId: TableId) : IO[TableResponse]
}

 class RestaurantRepositoryImpl(Db : Transactor[IO]) extends RestaurantRepository{
   private val random =  scala.util.Random
   val query = SQLQuery
   query.initSchema.run.transact(Db).unsafeRunSync()
   query.initDB.run.transact(Db).unsafeRunSync()
   def r:Int = 1 + random.nextInt(14)
   val logger = Logger("RestaurantRepository")
   val makeOrderReq : (Seq[String], Int) => List[RestaurantOrder] = (seq,t) => seq.map(x=>RestaurantOrder(x,t,r)).toList
   val makeDeleteReq : (Seq[String], Int) => List[DeleteOrder] = (seq,t) => seq.map(x=>DeleteOrder(x,t)).toList

  override def addItemsToTable(items: Seq[ItemName], tableId: TableId): IO[TableResponse] = {
    query.insertItems(makeOrderReq(items,tableId)).transact(Db).attempt.map{
      case Left(e) => logger.error("Unable to add items to table",e.getCause)
        ResponseBase.withError(Some("Sorry, our system has encountered some technical issues"))
      case Right(x) if(x==items.length)=> TableResponse(items = items.iterator.map(x=> Item(x,r)).toSeq,success = true)
      case Right(_) => ResponseBase.withError(Some("Sorry, your order could not be processed"))
    }
  }

  override def deleteOrder(items : Seq[ItemName], tableId: TableId) : IO[ItemDeleteResponse] = {
    query.deleteOrder(makeDeleteReq(items,tableId)).transact(Db).attempt.map{
      case Left(e) => logger.error(s"Unable to delete items for table $tableId",e.getCause)
        ItemDeleteResponse(success = false,Some("Sorry, our system has encountered some technical issues"))
      case Right(x) if(x==items.length) => ItemDeleteResponse(success = true)
      case Right(_) => ItemDeleteResponse(false,Some("Sorry, your order could not be processed"))
    }
  }

  override def getItemsInTable(tableId: TableId):  IO[TableResponse] = {
    query.queryItems(tableId).transact(Db).attempt.map{
        case Left(e) => logger.error(s"Unable to query items for table : $tableId",e.getCause)
          ResponseBase.withError(Some("Sorry, your order could not be processed"))
        case Right(x) => TableResponse(items = x, success = true)
      }
  }

}

case class RestaurantOrder(itemName : ItemName, tableId : Int, timeToPrepare : Int)

case class DeleteOrder(itemName: ItemName, tableId: Int)

object RestaurantRepository {
  def apply(): RestaurantRepository = new RestaurantRepositoryImpl(DBProxy.db)
}

