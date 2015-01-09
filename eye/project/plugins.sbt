// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.4")

//addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.0.2")

//resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
//
//addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")