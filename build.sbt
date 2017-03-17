lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "com.example",
  mainClass in (Compile,run) := Some("Hello")
)

libraryDependencies ++= {
  	Seq(
  	    "org.apache.kafka" % "kafka_2.11" % "0.10.2.0"
  	)
}

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases"
                )



scalacOptions ++= Seq("-unchecked", "-deprecation")

scalaVersion := "2.11.6"
