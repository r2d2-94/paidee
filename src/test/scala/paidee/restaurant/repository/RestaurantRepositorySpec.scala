package paidee.restaurant.repository

import org.scalatest.FunSuite

class RestaurantRepositorySpec extends FunSuite  {

  val testMinutes = 1
  val tableId  = 1
  private val testItem = Seq("pakoda","poha")
  val repo = new RestaurantRepositoryImpl{
    override def r = testMinutes
  }
/*test  test("testMakeOrder") {
    assert(repo.makeOrderReq(testItem,tableId).contains(Re)z
    ))
  }
  test("testDeleteOrder") {
    testItem.map( _ =>
      assert(repo.makeDeleteReq(testItem,tableId).contains(DeleteOrder(_,tableId))
      ))
  }*/
}
