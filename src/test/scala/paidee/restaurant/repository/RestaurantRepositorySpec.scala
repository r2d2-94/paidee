package paidee.restaurant.repository

import org.scalatest.FunSuite
import org.scalatestplus.mockito.MockitoSugar

class RestaurantRepositorySpec extends FunSuite  with MockitoSugar{

  val testMinutes = 1
  val tableId  = 1
  private val testItem = Seq("pakoda","poha")
  private val repo = new RestaurantRepositoryImpl{
    override def r = testMinutes
  }
  test("testMakeOrder") {
    val test = repo.makeOrderReq(testItem,tableId)
    assert(test.length==2)
    assert(test.contains(RestaurantOrder(testItem.head,tableId,testMinutes)))
    assert(test.contains(RestaurantOrder(testItem.tail.head,tableId,testMinutes)))
  }
  test("testDeleteOrder") {
    val test =  repo.makeDeleteReq(testItem,tableId)
      assert(repo.makeDeleteReq(testItem,tableId).length==2)
    assert(test.contains(DeleteOrder(testItem.head,tableId)))
    assert(test.contains(DeleteOrder(testItem.tail.head,tableId)))
  }

}