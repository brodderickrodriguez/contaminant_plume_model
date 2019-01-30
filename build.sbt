enablePlugins(org.nlogo.build.NetLogoExtension)

name := "plume-model"
version := "0.1"
scalaVersion := "2.12.0"

netLogoExtName      := "plume-model"
netLogoClassManager := "PlumeModelExtension"
netLogoZipSources   := false
isSnapshot := true

publishMavenStyle in ThisBuild := false
scalaSource in Compile := baseDirectory.value / "src"
scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings", "-encoding", "us-ascii")
netLogoVersion := "6.0.4"