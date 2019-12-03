package paidee.restaurant.repository

import org.scalatest.FunSuite
class RestaurantRepositorySpec extends FunSuite {

  val testMinutes = 1
  val tableId  = 1
  private val testItem = Seq("pakoda","poha")
  private val repo = new RestaurantRepositoryImpl(IntegrationTest.db){
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
