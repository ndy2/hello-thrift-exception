import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util._
import hello.{MyThriftException, PingService}

object PingServiceImpl extends PingService.MethodPerEndpoint {

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
  def scheduleAsyncJob(depth: Int): Future[Unit] = {
    timer.schedule(Time.now) {
      // 비동기 작업 실행
      while (true) {
        println(s"Async Job is running... depth : $depth")
        Thread.sleep(1000L)
      }
    }

    // 작업이 예약되었다는 메시지 출력
    Future.Unit
  }

  def main(): Unit = {

    // 비동기 작업 스케줄링
    scheduleAsyncJob(0)

    println("run server")
    val server = Thrift.server
      .withLabel("demo-server")
      .serveIface("localhost:8080", PingServiceImpl)
    Await.ready(server)
  }
}
