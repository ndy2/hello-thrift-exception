import DemoServer.statsReceiver
import com.twitter.finagle.{Thrift, ThriftMux}
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.stats.{Counter, MetricBuilder, Stat, StatsReceiver}
import com.twitter.server.TwitterServer
import com.twitter.util._
import hello.{MyThriftException, PingService}

object PingServiceImpl extends PingService.MethodPerEndpoint {

  val counter: Counter = statsReceiver
    .scope("srv", "demo")
    .counter("counter")

  // see ExceptionStatsHandler
  val stat: Stat = statsReceiver
    .scope("srv", "demo")
    .stat("stat")

  override def ping(): Future[String] = {
    stat.add(1.0.toFloat)
    stat.metadata
    counter.incr()
    Future.value("pong")
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
}

object DemoServer extends TwitterServer {

  val timer = new JavaTimer(isDaemon = true)

  // 비동기 작업을 스케줄링하는 메서드
  def scheduleAsyncJob(): Future[Unit] = {
    timer.schedule(Time.now) {
      // 비동기 작업 실행
      while (true) {
        println(s"Async Job is running...")
        Thread.sleep(60000L) // print above line once in a minute
      }
    }

    // 작업이 예약되었다는 메시지 출력
    Future.Unit
  }

  def main(): Unit = {
    // 비동기 작업 스케줄링
    scheduleAsyncJob()

    println("run server")
    val server = ThriftMux.server
      .withLabel("demo-server")
      .serveIface("localhost:8080", PingServiceImpl)
    Await.ready(server)
  }
}
