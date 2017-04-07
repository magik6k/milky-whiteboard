resolvers += "ipfs" at "https://ipfs.io/ipfs/QmdGWsWEsvZNRJQonfGftgv4qs9XkzU42143BJez1gUj6e"

lazy val root = (project in file(".")).settings(
  npmDependencies in Compile += "ipfs" -> "0.23.1",
  webpackConfigFile := Some(baseDirectory.value / "webpack.config.js"),

  scalaVersion := "2.12.1",
  libraryDependencies ++= Seq(
    "eu.devtty" %%% "js-ipfs-api" % "0.1-SNAPSHOT",
    "org.scala-js" %%% "scalajs-dom" % "0.9.1"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
