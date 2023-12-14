namespace java hello
#@namespace scala hello

include "Exceptions.thrift"

service PingService {

  string throwNpe()

  string throwMteNotDeclared()

  string throwMteDeclared() throws (
    1: Exceptions.MyThriftException e
  )

  string futureNpe()

  string futureMteNotDeclared()

  string futureMteDeclared() throws (
    1: Exceptions.MyThriftException e
  )
}
