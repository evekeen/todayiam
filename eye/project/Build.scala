import sbt.Keys._
import sbt._
import play.twirl.sbt.Import._

object ApplicationBuild extends Build {

  val appName = "eye"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "todayiam" % "watchdog" % "1.0-SNAPSHOT"
  )

  val main = Project(appName, file(".")).enablePlugins(play.PlayScala).settings(
    resolvers += Resolver.mavenLocal,
    version := appVersion,
    libraryDependencies ++= appDependencies,
    TwirlKeys.templateImports ++= Seq(
      "java.util.List",
      "org.springframework.social.twitter.api.Tweet"
    )
  )
}
