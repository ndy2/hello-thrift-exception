import com.twitter.finagle.ThriftMux
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.routing.HttpRouter

object DemoServer extends HttpServer {

  // configure components
  private val pingService        = new PingService
  private val pingMethodEndPoint = new PingMethodEndPoint(pingService)
  private val pingController     = new PingController(pingService)

  override protected def configureHttp(router: HttpRouter): Unit = {
    router
      .add(pingController)
  }

  override protected def postInjectorStartup(): Unit = {
    super.postInjectorStartup()
    val server = ThriftMux.server
      .withLabel("demo-server")
      .serveIface("localhost:8080", pingMethodEndPoint)
    server.announce("flag!ping")
  }
}
