name := "SCALA_Play_Framework_Example"
 
version := "1.0" 
      
lazy val `scala_play_framework_example` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc , ehcache , ws , specs2 % Test , guice)
libraryDependencies += filters

libraryDependencies += "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5"

libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.47"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )