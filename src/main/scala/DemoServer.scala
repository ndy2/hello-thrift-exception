import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import hello.{MyThriftException, PingService}

object PingServiceImpl extends PingService.MethodPerEndpoint {

  override def throwNpe(): Future[String] = throw new NullPointerException("throwNpe")

  override def throwMteNotDeclared(): Future[String] = throw new MyThriftException("throwMteNotDeclared")

  override def throwMteDeclared(): Future[String] = throw new MyThriftException("throwMteDeclared")

  override def futureNpe(): Future[String] = Future.exception(new NullPointerException("futureNpe"))

  override def futureMteNotDeclared(): Future[String] = Future.exception(new MyThriftException("futureMteNotDeclared"))

  override def futureMteDeclared(): Future[String] = Future.exception(new MyThriftException("futureMteDeclared"))
}

object DemoServer extends TwitterServer {

  def main(): Unit = {
    val server = Thrift.server
      .withLabel("demo-server")
      .serveIface("localhost:8080", PingServiceImpl)
    Await.ready(server)
  }
}
