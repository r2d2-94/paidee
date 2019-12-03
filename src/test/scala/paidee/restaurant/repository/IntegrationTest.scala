package paidee.restaurant.repository

import cats.effect.IO
import doobie._
import doobie.implicits._
import org.scalatest.{BeforeAndAfter,FunSuite}

import scala.concurrent.ExecutionContext
class IntegrationTest extends FunSuite with BeforeAndAfter {

  val items = Seq("item1","item2")
  val repo = new RestaurantRepositoryImpl(IntegrationTest.db)
  before (
    sql"""DROP TABLE IF EXISTS myproducts.order""".update.run.transact(IntegrationTest.db).unsafeRunAsyncAndForget()
  )
  test("table should have no order if same items are added and deleted") {
  val addedItems = repo.addItemsToTable(items,1)
    val removedItems =  repo.deleteOrder(items,1)
    val queriedItems =  repo.getItemsInTable(1)
    assert(addedItems.unsafeRunSync().items.size==items.size)
    assert(removedItems.unsafeRunSync().success)
    assert(queriedItems.unsafeRunSync().items.isEmpty)
  }
  test("should be able to query if an item was added") {
    val addedItems = repo.addItemsToTable(items.tail,1)
    val queriedItems =  repo.getItemsInTable(1)
    assert(addedItems.unsafeRunSync().items.size==1)
    assert(queriedItems.unsafeRunSync().items.head.itemName == items.tail.head)
  }
}
object IntegrationTest {
  implicit val cs = IO.contextShift(ExecutionContext.global)
  val db = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql:test", "postgres", "postgres"
  )
}
