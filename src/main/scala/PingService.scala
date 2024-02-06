import DemoServer.statsReceiver
import com.twitter.finagle.stats.Counter
import com.twitter.util.Future
import hello.{ MyThriftException, RequestContext }

import scala.util.Random

class PingService {

  private val random = new Random()

  val counter: Counter = statsReceiver
    .scope("srv", "demo")
    .counter("counter")

  def ping(): Future[String] = {
    println("ping")

    counter.incr()
    Future.value("pong")
  }

  def counter(name: String): Future[String] = {
    println("counter")

    statsReceiver
      .scope("srv", "demo")
      .counter(name)
      .incr()

    Future.value("counter")
  }

  def randomCounter(name: String): Future[String] = {
    println("randomCounter")
    val aOrB = if (random.nextBoolean()) "a"; else { "b" }
    statsReceiver
      .scope("srv", "demo", name, aOrB)
      .counter()
      .incr()
    Future.value("randomCounter")
  }

  def throwNpe(): Future[String] = {
    println("throwNpe")
    throw new NullPointerException("throwNpe")
  }

  def throwMteNotDeclared(): Future[String] = {
    println("throwMteNotDeclared")
    throw new MyThriftException("throwMteNotDeclared")
  }

  def throwMteDeclared(): Future[String] = {
    println("throwMteDeclared")
    throw new MyThriftException("throwMteDeclared")
  }

  def futureNpe(): Future[String] = {
    println("futureNpe")
    Future.exception(new NullPointerException("futureNpe"))
  }

  def futureMteNotDeclared(): Future[String] = {
    println("futureMteNotDeclared")
    Future.exception(new MyThriftException("futureMteNotDeclared"))
  }

  def futureMteDeclared(): Future[String] = {
    println("futureMteDeclared")
    Future.exception(new MyThriftException("futureMteDeclared"))
  }

  def getToken(context: RequestContext): Future[String] = {
    println("getToken")
    Future.value(s"${context.client}.${context.username}")
  }

  def pingWithToken(token: String): Future[String] = {
    print(s"pingWithToken($token)")
    Future.value("pong")
  }
}
