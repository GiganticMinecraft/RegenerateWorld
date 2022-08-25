import sbt.Keys.baseDirectory

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / version := "0.1.0"

resolvers ++= Seq(
  "maven.elmakers.com" at "https://maven.elmakers.com/repository/", // spigot
  "Sonatype OSS" at "https://s01.oss.sonatype.org/content/groups/public/",
  "Multiverse" at "https://repo.onarandombox.com/content/repositories/multiverse/"
)

libraryDependencies ++= Seq(
  "org.spigotmc" % "spigot-api" % "1.12.2-R0.1-SNAPSHOT" % "provided",
  "com.onarandombox.multiversecore" % "Multiverse-Core" % "2.5.0" % "provided",
  "com.beachape" %% "enumeratum" % "1.7.0"
)

excludeDependencies ++= Seq(
  ExclusionRule(organization = "org.bukkit", name = "bukkit"),
  ExclusionRule(organization = "org.bukkit", name = "craftbukkit"),
  ExclusionRule(organization = "com.pneumaticraft.commandhandler", name = "CommandHandler")
)

lazy val root = (project in file("."))
  .settings(
    name := "RegenerateWorld",
    scalaVersion := "2.13.8",
    assembly / assemblyOutputPath := baseDirectory.value / "target" / "build" / s"${name.value}-${version.value}.jar",
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
