package paidee.restaurant

import java.util.concurrent.Executors

import scala.concurrent.duration.{FiniteDuration, SECONDS}
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect._
import cats.implicits._
import org.http4s.{Method, Request, Uri}
import org.http4s.client.blaze.BlazeClientBuilder
import io.circe.generic.auto._
import org.http4s.circe._
import io.circe.syntax._
import paidee.restaurant.models.{TableItemsRequest, TableRequest, TableResponse}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random


object Main extends IOApp {
  val items = Seq("pakoda","poha","golgappa","nachos","curry","burrito","naan","rasmalai","dosa","uttapam","tea","gujiya","papdi")
  val length = items.length
  val rand = scala.util.Random
  def run(args: List[String]) = {
    def queryOrders() = {
      val tableId =  1 + rand.nextInt(99)
      BlazeClientBuilder[IO](global)
        .resource
        .use { client =>
          val req = Request[IO](method = Method.POST, uri = Uri.uri("http://localhost:8080/v1/table/orders"))
            .withEntity(TableRequest(tableId).asJson)
          client.expect(req)
        }
    }
    def createOrders() = {
      val tableId =  1 + rand.nextInt(99)
      val takeItems = rand.nextInt(items.length)
      BlazeClientBuilder[IO](global)
        .resource
        .use { client =>
          val req = Request[IO](method = Method.PUT, uri = Uri.uri("http://localhost:8080/v1/table/addItems"))
            .withEntity(TableItemsRequest(Random.shuffle(items).take(takeItems),tableId).asJson)
          client.expect(req)
        }
    }
    def deleteOrders() = {
      val tableId =  1 + rand.nextInt(99)
      val takeItems = rand.nextInt(items.length)
      BlazeClientBuilder[IO](global)
        .resource
        .use { client =>
          val req = Request[IO](method = Method.DELETE, uri = Uri.uri("http://localhost:8080/v1/table/removeItems"))
            .withEntity(TableItemsRequest(Random.shuffle(items).take(takeItems),tableId).asJson)
          client.expect(req)
        }
    }
    val t = new java.util.Timer()
    val task = new java.util.TimerTask {
      def run() = 1 to 3 foreach { _ =>
        queryOrders().unsafeRunAsyncAndForget()
        createOrders().unsafeRunAsyncAndForget()
        deleteOrders().unsafeRunAsyncAndForget()
      }
    }
    t.schedule(task, 1000L, 1000L)
    // start server
    RestaurantServer.stream.compile.drain.as(ExitCode.Success)
  }
}