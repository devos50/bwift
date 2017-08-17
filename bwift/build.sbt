name := """bwift"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies += guice

libraryDependencies ++= Seq(
"mysql" % "mysql-connector-java" % "5.1.18"
)

libraryDependencies ++= Seq(
  javaWs
)