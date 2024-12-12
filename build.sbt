lazy val sqlcGenerate = taskKey[Unit]("run sqlc generate")
lazy val generatedCodeTypeScriptDef = taskKey[Unit]("generate d.ts")

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

generatedCodeTypeScriptDef := {
  import scala.sys.process._

  val command = Seq("bash", "-c", "cd database && npm run build:declaration")
  val exitCode = command.!

  if (exitCode != 0) {
    throw new IllegalStateException(s"Command '${command.mkString(" ")}' failed with exit code $exitCode")
  } else {
    println("d.ts file generate completed successfully!")
  }
}

generatedCodeTypeScriptDef := (generatedCodeTypeScriptDef dependsOn sqlcGenerate).value
Compile / compile := ((Compile / compile) dependsOn generatedCodeTypeScriptDef).value

lazy val root = project.in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
  .settings(
    externalNpm := {
      baseDirectory.value
    },
    stOutputPackage := "scalavitesqlc",
    stIgnore += "node",
    scalaVersion := "3.3.3",
    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
    }
  )
