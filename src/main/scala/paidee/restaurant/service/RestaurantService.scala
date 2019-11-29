package paidee.restaurant.service

import cats.effect.IO
import paidee.restaurant.models.{ItemDeleteResponse, TableItemsRequest, TableRequest, TableResponse}
import paidee.restaurant.repository.RestaurantRepository

 trait RestaurantService{
  def addItem(request: TableItemsRequest): IO[TableResponse]
  def deleteItem(request: TableItemsRequest) : IO[ItemDeleteResponse]
  def queryItem(request : TableRequest) : IO[TableResponse]
}

class RestaurantServiceImpl extends RestaurantService {
  def addItem(request: TableItemsRequest): IO[TableResponse] =
    RestaurantRepository.apply().addItemsToTable(request.items,request.tableId)

  def deleteItem(request: TableItemsRequest) : IO[ItemDeleteResponse] =
    RestaurantRepository.apply().deleteOrder(request.items,request.tableId)

  def queryItem(request : TableRequest) : IO[TableResponse] =
    RestaurantRepository.apply().getItemsInTable(request.tableId)
}
object RestaurantService {
  def apply(): RestaurantService = new RestaurantServiceImpl
}