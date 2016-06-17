import ReleaseTransformations._
import sbtrelease._
import sbtrelease.ReleaseStateTransformations.{setReleaseVersion=>_,_}

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

releaseVersion <<= (releaseVersionBump) ( bumper=>{
  ver => Version(ver)
         .map(_.withoutQualifier)
         .map(_.bump(bumper).string).getOrElse(versionFormatError)
})

// Auto-Update Version Test
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,              // : ReleaseStep
  inquireVersions,                        // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,                      // : ReleaseStep
  commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
  tagRelease,                             // : ReleaseStep
  //publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion,                         // : ReleaseStep
  commitNextVersion                      // : ReleaseStep
  //pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
)