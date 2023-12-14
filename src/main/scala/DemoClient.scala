import com.twitter.finagle.Thrift
import com.twitter.util.Await
import hello.PingService

object DemoClient extends App {

  private val client: PingService.MethodPerEndpoint = Thrift.client
    .withLabel("demo-client")
    .build[PingService.MethodPerEndpoint](s"localhost:8080")

//  Await.result(client.throwNpe())
//  Await.result(client.throwMteNotDeclared())
//  Await.result(client.throwMteDeclared())
//  Await.result(client.futureNpe())
//  Await.result(client.futureMteNotDeclared())
  Await.result(client.futureMteDeclared())
}
