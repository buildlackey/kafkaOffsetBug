lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "com.example",
  mainClass in (Compile,run) := Some("Hello")
)

libraryDependencies ++= {
  	Seq(
  	    "org.apache.kafka" % "kafka_2.10" % "0.8.2.1" 
  	)
}

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases"
                )
 
scalacOptions ++= Seq("-unchecked", "-deprecation")
