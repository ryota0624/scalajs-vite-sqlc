lazy val sqlcGenerate = taskKey[Unit]("run sqlc generate")
sqlcGenerate := {
  import scala.sys.process._

  val command = Seq("bash", "-c", "cd database && sqlc generate")
  val exitCode = command.!

  if (exitCode != 0) {
    throw new IllegalStateException(s"Command '${command.mkString(" ")}' failed with exit code $exitCode")
  } else {
    println("sqlc generate completed successfully!")
  }
}


lazy val root = project.in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    Compile / compile := ((Compile / compile) dependsOn sqlcGenerate).value,
    scalaVersion := "3.3.3",
    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

  )
