import com.twitter.util.Future
import hello.{PingService, RequestContext}

class PingMethodEndPoint(service: PingService) extends PingService.MethodPerEndpoint {

  override def ping(): Future[String] = service.ping()

  override def counter(name: String): Future[String] = service.counter(name)

  override def randomCounter(name: String): Future[String] = service.randomCounter(name)

  override def throwNpe(): Future[String] = service.throwNpe()

  override def throwMteNotDeclared(): Future[String] = service.throwMteNotDeclared()

  override def throwMteDeclared(): Future[String] = service.throwMteNotDeclared()

  override def futureNpe(): Future[String] = service.futureNpe()

  override def futureMteNotDeclared(): Future[String] = service.futureMteNotDeclared()

  override def futureMteDeclared(): Future[String] = service.futureMteDeclared()

  override def getToken(context: RequestContext): Future[String] = service.getToken(context)

  override def pingWithToken(token: String): Future[String] = service.pingWithToken(token)
}
