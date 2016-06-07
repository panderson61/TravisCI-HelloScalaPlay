name := "TravisCI-HelloScalaPlay"

version := "1.0"

lazy val `play2template` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test , "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  