import DemoServer.statsReceiver
import com.twitter.finagle.ThriftMux
import com.twitter.finagle.http.Request
import com.twitter.finagle.stats._
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.{Controller, HttpServer}
import com.twitter.scrooge.TJSONProtocolThriftSerializer
import com.twitter.util._
import hello.{MyThriftException, PingService, RequestContext}

import scala.util.Random

object PingServiceImpl extends PingService.MethodPerEndpoint {

  private val random = new Random()

  val counter: Counter = statsReceiver
    .scope("srv", "demo")
    .counter("counter")

  override def ping(): Future[String] = {
    counter.incr()
    Future.value("pong")
  }

  override def counter(name: String): Future[String] = {
    statsReceiver
      .scope("srv", "demo")
      .counter(name)
      .incr()

    Future.value("counter")
  }

  override def randomCounter(name: String): Future[String] = {
    println("randomCounter")
    val aOrB = if (random.nextBoolean()) "a"; else { "b" }
    statsReceiver
      .scope("srv", "demo", name, aOrB)
      .counter()
      .incr()
    Future.value("randomCounter")
  }

  override def throwNpe(): Future[String] = {
    println("throwNpe")
    throw new NullPointerException("throwNpe")
  }

  override def throwMteNotDeclared(): Future[String] = {
    println("throwMteNotDeclared")
    throw new MyThriftException("throwMteNotDeclared")
  }

  override def throwMteDeclared(): Future[String] = {
    println("throwMteDeclared")
    throw new MyThriftException("throwMteDeclared")
  }

  override def futureNpe(): Future[String] = {
    println("futureNpe")
    Future.exception(new NullPointerException("futureNpe"))
  }

  override def futureMteNotDeclared(): Future[String] = {
    println("futureMteNotDeclared")
    Future.exception(new MyThriftException("futureMteNotDeclared"))
  }

  override def futureMteDeclared(): Future[String] = {
    println("futureMteDeclared")
    Future.exception(new MyThriftException("futureMteDeclared"))
  }

  override def getToken(context: RequestContext): Future[String] = {
    println("getToken")
    Future.value(s"${context.client}.${context.username}")
  }

  override def pingWithToken(token: String): Future[String] = {
    print(s"pingWithToken($token)")
    Future.value("pong")
  }
}

class PingController extends Controller {
  private val contextSerializer = TJSONProtocolThriftSerializer(RequestContext)

  get(route = "/") { req: Request => Future.value("hello") }
  post(route = "/ping") { req: Request => PingServiceImpl.ping() }

  post(route = "/counter") { req: Request =>
    PingServiceImpl.counter(
      name = req.getParam("name")
    )
  }

  post(route = "/randomCounter") { req: Request =>
    PingServiceImpl.randomCounter(
      name = req.getParam("name")
    )
  }

  post(route = "/throwNpe") { req: Request => PingServiceImpl.throwNpe() }
  post(route = "/throwMteNotDeclared") { req: Request => PingServiceImpl.throwMteNotDeclared() }
  post(route = "/throwMteDeclared") { req: Request => PingServiceImpl.throwMteDeclared() }
  post(route = "/futureNpe") { req: Request => PingServiceImpl.futureNpe() }
  post(route = "/futureMteNotDeclared") { req: Request => PingServiceImpl.futureMteNotDeclared() }
  post(route = "/futureMteDeclared") { req: Request => PingServiceImpl.futureMteDeclared() }

  post(route = "/getToken") { req: Request =>
    val contentString = req.getContentString()
    println(s"contentString = ${contentString}")

    val requestContext = contextSerializer.fromString(contentString)
    PingServiceImpl.getToken(requestContext)
  }

  post(route = "/pingWithToken") { req: Request =>
    PingServiceImpl.pingWithToken(
      token = req.getParam("token")
    )
  }
}

object DemoServer extends HttpServer {

  override protected def configureHttp(router: HttpRouter): Unit = {
    router
      .add[PingController]
  }

  override protected def postInjectorStartup(): Unit = {
    super.postInjectorStartup()

    println("run thrift server")
    val server = ThriftMux.server
      .withLabel("demo-server")
      .serveIface("localhost:8080", PingServiceImpl)
    server.announce("flag!ping")
  }
}
