resolvers += "ipfs" at "https://ipfs.io/ipfs/QmRaFZh5Wu4TJZRPfKgjXqH7PbDr6xTfiZ95wfXBoZNrZn"

lazy val root = (project in file(".")).settings(
  npmDependencies in Compile += "ipfs" -> "0.23.1",
  webpackConfigFile := Some(baseDirectory.value / "webpack.config.js"),

  scalaVersion := "2.12.1",
  libraryDependencies ++= Seq(
    "eu.devtty" %%% "js-ipfs-api" % "0.1-SNAPSHOT"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
