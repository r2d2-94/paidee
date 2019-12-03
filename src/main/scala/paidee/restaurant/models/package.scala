package paidee.restaurant
import org.http4s.circe.CirceEntityCodec._

package object models {
 type TableId = Int
 type ItemName = String

 case class Item(itemName : ItemName , timeToPrepare : Int)

 case class TableItemsRequest(items : Seq[ItemName],tableId : TableId)

 case class TableRequest(tableId: TableId)

 case class TableResponse(items : Seq[Item],
                          override val errorMessage : Option[String] = None,
                          override val success : Boolean)
   extends ResponseBase

 trait ResponseBase {
  def success : Boolean = false
  def errorMessage : Option[String] = None
 }
 case class ItemDeleteResponse (override val success : Boolean,
                                override val errorMessage: Option[String] = None) extends ResponseBase

object  ResponseBase{
 def withError(message : Option[String]) = TableResponse(items = Seq.empty,errorMessage = message,success = false)
 def withSuccess() = TableResponse(items = Seq.empty,errorMessage = None,success = true)
}

}
