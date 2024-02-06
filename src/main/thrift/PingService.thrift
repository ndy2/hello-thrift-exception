namespace java hello
#@namespace scala hello

include "Exceptions.thrift"
include "Types.thrift"

service PingService {

  string ping()

  string counter(
    1: string name
  )

  string randomCounter(
    1: string name
  )

  string throwNpe()

  string throwMteNotDeclared()

  string throwMteDeclared() throws (
    1: Exceptions.MyThriftException e
  )

  string futureNpe()

  string futureMteNotDeclared()

  string futureMteDeclared() throws (
    1: Exceptions.MyThriftException mte
    2: Exceptions.YourThriftException yte
  )

  string getToken(
    1: Types.RequestContext context
  )

  string pingWithToken(
    1: string token
  )
}
