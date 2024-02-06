ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.12"

lazy val versions = new {
  val finatra = "21.2.0"
}

lazy val root = (project in file("."))
  .settings(
    name := "hello-thrift-exception",
    libraryDependencies ++= Seq(
      "org.apache.thrift" % "libthrift"          % "0.10.0",
      "com.twitter"      %% "twitter-server"     % versions.finatra,
      "com.twitter"      %% "finagle-thriftmux"  % versions.finatra,
      "com.twitter"      %% "scrooge-core"       % versions.finatra,
      "com.twitter"      %% "util-core"          % versions.finatra,
      "com.twitter"      %% "finagle-core"       % versions.finatra,
      "com.twitter"      %% "finatra-http"       % versions.finatra,
      "com.twitter"      %% "scrooge-serializer" % versions.finatra,
      "org.scalaj"       %% "scalaj-http"        % "2.4.2",
      "ch.qos.logback"    % "logback-classic"    % "1.4.14"
    )
  )
