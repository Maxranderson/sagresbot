name := "telegram-sagres-bot"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "com.github.pengrad" % "java-telegram-bot-api" % "4.9.0" withJavadoc,
  "org.xerial" % "sqlite-jdbc" % "3.28.0",
  "io.getquill" %% "quill-jdbc" % "3.5.3",
  "org.flywaydb" % "flyway-core" % "7.0.0",
  "org.postgresql" % "postgresql" % "42.2.8",
)