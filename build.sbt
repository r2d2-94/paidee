val Http4sVersion = "0.21.0-M3"
val CirceVersion = "0.12.0-M4"
val Specs2Version = "4.7.0"
val LogbackVersion = "1.2.3"
val ConfigVersion = "1.3.1"
val PostgresqlVersion = "9.1-901-1.jdbc4"
val CatsCoreVersion = "2.0.0"
val DoobieVersion = "0.8.6"


lazy val root = (project in file("."))
  .settings(
    organization := "paidee",
    name := "restaurant",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "org.specs2"      %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "com.typesafe" % "config" % ConfigVersion,
      "postgresql" % "postgresql" % PostgresqlVersion,
      "org.typelevel" %% "cats-core" % CatsCoreVersion,
      "org.tpolecat" %% "doobie-core"  % DoobieVersion,
      "org.tpolecat" %% "doobie-quill"     % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres"  % DoobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % DoobieVersion % "test"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
  "-Ypartial-unification"
)
