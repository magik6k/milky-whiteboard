resolvers += "ipfs" at "https://ipfs.io/ipfs/QmcpvU82FefRpk49AMqkjaWEUptParuEobMWPQqGejFvnh"

lazy val root = (project in file(".")).settings(
  webpackConfigFile := Some(baseDirectory.value / "webpack.config.js"),

  scalaVersion := "2.12.1",
  libraryDependencies ++= Seq(
    "eu.devtty" %%% "js-ipfs-api" % "0.2.5-SNAPSHOT",
    "org.scala-js" %%% "scalajs-dom" % "0.9.1"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
