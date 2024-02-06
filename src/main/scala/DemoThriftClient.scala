import com.twitter.finagle.Thrift
import com.twitter.util.Await
import hello.{PingService, RequestContext}

object DemoThriftClient extends App {

  private val client: PingService.MethodPerEndpoint = Thrift.client
    .withLabel("demo-thrift-client")
    .build[PingService.MethodPerEndpoint](s"localhost:8080")

  val context = RequestContext("demo-thrift", "ndy")
  println(Await.result(client.getToken(context)))

  for (_ <- 1 to 5) {
    println(Await.result(client.randomCounter("random-counter-1")))
    println(Await.result(client.randomCounter("random-counter-2")))
    println(Await.result(client.randomCounter("random-counter-3")))
    println(Await.result(client.randomCounter("random-counter-4")))

    println(Await.result(client.randomCounter("random-counter-2")))
    println(Await.result(client.randomCounter("random-counter-2")))
    println(Await.result(client.randomCounter("random-counter-2")))
  }
}
