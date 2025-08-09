import ResourceFilter.filterResources
import sbt.Keys.baseDirectory

// region Dependencies

resolvers ++= Seq(
  "papermc" at "https://repo.papermc.io/repository/maven-public/",
  "Sonatype OSS" at "https://s01.oss.sonatype.org/content/groups/public/",
  "Multiverse" at "https://repo.onarandombox.com/content/groups/public/"
)

val providedDependencies = Seq(
  "io.papermc.paper" % "paper-api" % "1.18.2-R0.1-SNAPSHOT",
  "com.onarandombox.multiversecore" % "Multiverse-Core" % "4.3.1"
).map(_ % "provided")

val embeddedDependencies = Seq("com.beachape" %% "enumeratum" % "1.9.0")

val testDependencies = Seq(
  "org.scalamock" %% "scalamock" % "7.4.1",
  "org.scalatest" %% "scalatest" % "3.2.19",
  "org.scalatestplus" %% "scalacheck-1-16" % "3.2.14.0"
).map(_ % "test")

libraryDependencies := providedDependencies ++ embeddedDependencies ++ testDependencies

excludeDependencies ++= Seq(
  ExclusionRule(organization = "org.bukkit", name = "bukkit"),
  ExclusionRule(organization = "org.bukkit", name = "craftbukkit"),
  ExclusionRule(organization = "com.pneumaticraft.commandhandler", name = "CommandHandler")
)

// endregion

// region プラグインJarに埋め込むリソースの処理

// This region is
// * licensed under GPL v3 (https://github.com/GiganticMinecraft/SeichiAssist/blob/2e2c33af7138b3f0f6137245ba389c7a98f92f23/LICENSE)
// * written in SeichiAssist (https://github.com/GiganticMinecraft/SeichiAssist/blob/398d224228b933f5523ceebf173f3fad46605cb8/build.sbt#L135-L171)

val tokenReplacementMap =
  settingKey[Map[String, String]]("Map specifying what tokens should be replaced to")

tokenReplacementMap := Map("name" -> name.value, "version" -> version.value)

val filesToBeReplacedInResourceFolder = Seq("plugin.yml")

val filteredResourceGenerator = taskKey[Seq[File]]("Resource generator to filter resources")

Compile / filteredResourceGenerator :=
  filterResources(
    filesToBeReplacedInResourceFolder,
    tokenReplacementMap.value,
    (Compile / resourceManaged).value,
    (Compile / resourceDirectory).value
  )

Compile / resourceGenerators += (Compile / filteredResourceGenerator)

Compile / unmanagedResources ++= Seq(baseDirectory.value / "LICENSE")

// トークン置換を行ったファイルをunmanagedResourcesのコピーから除外する
unmanagedResources / excludeFilter :=
  filesToBeReplacedInResourceFolder.foldLeft((unmanagedResources / excludeFilter).value)(
    _.||(_)
  )

// endregion

// region Other settings

lazy val root = (project in file(".")).settings(
  name := "RegenerateWorld",
  scalaVersion := "2.13.16",
  assembly / assemblyJarName := s"${name.value}-${version.value}.jar",
  assembly / test := (Test / test).value, // assmeblyの中でtestをする
  // scalafixがsemanticdbを必要とする
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,
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
  javacOptions ++= Seq("-encoding", "utf8"),
  // build.sbtそのほかビルドの設定が変わったときにsbtを自動リロードさせる
  Global / onChangedBuildSource := ReloadOnSourceChanges,
  // テストが落ちた時にスタックトレースを表示する
  Compile / testOptions += Tests.Argument("-oS")
)

// endregion
