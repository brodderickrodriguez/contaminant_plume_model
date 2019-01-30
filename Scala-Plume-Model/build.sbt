enablePlugins(org.nlogo.build.NetLogoExtension)

name := "Scala-Plume-Model"
version := "0.1"
scalaVersion := "2.12.0"

netLogoExtName      := "plume-scala"
netLogoClassManager := "ExtensionManager"
netLogoZipSources   := false

scalaSource in Compile := baseDirectory.value / "src"
scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings", "-encoding", "us-ascii")
netLogoVersion := "6.0.2-M1"