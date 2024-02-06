import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.scrooge.TJSONProtocolThriftSerializer
import hello.RequestContext

class PingController(service: PingService) extends Controller {
  private val contextSerializer = TJSONProtocolThriftSerializer(RequestContext)

  post(route = "/ping") { req: Request => service.ping() }

  post(route = "/counter") { req: Request =>
    service.counter(
      name = req.getParam("name")
    )
  }

  post(route = "/randomCounter") { req: Request =>
    service.randomCounter(
      name = req.getParam("name")
    )
  }

  post(route = "/throwNpe") { req: Request => service.throwNpe() }
  post(route = "/throwMteNotDeclared") { req: Request => service.throwMteNotDeclared() }
  post(route = "/throwMteDeclared") { req: Request => service.throwMteDeclared() }
  post(route = "/futureNpe") { req: Request => service.futureNpe() }
  post(route = "/futureMteNotDeclared") { req: Request => service.futureMteNotDeclared() }
  post(route = "/futureMteDeclared") { req: Request => service.futureMteDeclared() }

  post(route = "/getToken") { req: Request =>
    val contentString = req.getContentString()
    println(s"contentString = ${contentString}")

    val requestContext = contextSerializer.fromString(contentString)
    service.getToken(requestContext)
  }

  post(route = "/pingWithToken") { req: Request =>
    service.pingWithToken(
      token = req.getParam("token")
    )
  }
}
