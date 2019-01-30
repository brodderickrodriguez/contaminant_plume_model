enablePlugins(org.nlogo.build.NetLogoExtension)

name := "Scala-Plume-Model"
version := "0.1"
scalaVersion := "2.12.8"

netLogoExtName := "plume-scala"
netLogoClassManager := "PlumeClassManager"
netLogoZipSources := false
isSnapshot := true
publishMavenStyle in ThisBuild := false

scalaSource in Compile := baseDirectory.value / "src"
scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings", "-encoding", "us-ascii")
netLogoTarget := org.nlogo.build.NetLogoExtension.directoryTarget(baseDirectory.value)
netLogoVersion := "6.0.4"

