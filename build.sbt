import ReleaseTransformations._

name := "TravisCI-HelloScalaPlay"

version := (version in ThisBuild).value

lazy val `play2template` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test , "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

//// Docker configuration

// setting a maintainer which is used for all packaging types
maintainer:= "JR Reyes <jayrtrooper@gmail.com>"

// exposing the play ports
dockerExposedPorts in Docker := Seq(9000, 9443)

// run this with: docker run -p 9000:9000 <name>:<version>

dockerRepository := Some("jayrtrooper")

dockerBaseImage := "java:8"

dockerUpdateLatest := true