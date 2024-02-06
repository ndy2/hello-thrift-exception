resolvers ++= Seq(
  "Twitter Maven" at "https://maven.twttr.com",
  "Finatra Repo" at "https://twitter.github.com/finatra"
)

addSbtPlugin("com.twitter" % "scrooge-sbt-plugin" % "21.4.0")