import hello.RequestContext
import org.apache.thrift.protocol.TJSONProtocol
import org.apache.thrift.transport.TMemoryBuffer
import scalaj.http.Http

object DemoHttpClient extends App {
  val context = RequestContext("demo-http", "ndy")
  val transport = new TMemoryBuffer(1024)
  val protocol  = new TJSONProtocol(transport)
  context.write(protocol)

  val encodedData = transport.getArray

  println(
    new String(encodedData)
  ) // "{"1":{"str":"demo-http"},"2":{"str":"ndy"}}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     "

  private val resp = Http("http://localhost:8888/getToken")
    .postData(encodedData)
    .asString

  println(
    s"resp = ${resp}"
  ) // "resp = HttpResponse(demo-http.ndy,200,Map(content-encoding -> Vector(gzip), content-length -> Vector(39), Content-Type -> Vector(text/plain; charset=utf-8), Status -> Vector(HTTP/1.1 200 OK)))"
}
