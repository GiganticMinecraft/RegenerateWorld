import sbt.Keys.baseDirectory
import ReleaseTransformations._

ThisBuild / scalaVersion := "2.13.8"

resolvers ++= Seq(
  "maven.elmakers.com" at "https://maven.elmakers.com/repository/", // spigot
  "Sonatype OSS" at "https://s01.oss.sonatype.org/content/groups/public/"
)

libraryDependencies ++= Seq(
  "org.spigotmc" % "spigot-api" % "1.12.2-R0.1-SNAPSHOT",
  "com.beachape" %% "enumeratum" % "1.7.0"
)

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("publishSigned"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

lazy val root = (project in file("."))
  .settings(
    name := "RegenerateWorld",
    assembly / assemblyOutputPath := baseDirectory.value / "target" / "build" / "RegenerateWorld.jar",
    excludeDependencies := Seq(ExclusionRule(organization = "org.bukkit", name = "bukkit")),
    scalacOptions ++= Seq(
      "-encoding",
      "utf8",
      "-unchecked",
      "-language:higherKinds",
      "-deprecation",
      "-Ypatmat-exhaust-depth",
      "320",
      "-Ymacro-annotations",
      "-Ywarn-unused"
    ),
    javacOptions ++= Seq("-encoding", "utf8")
  )
