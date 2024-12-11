package adventcaaner_2024

import scala.scalajs.js
import scalavitesqlc.database.generatedAuthorsSqlMod.{CreateAuthorArgs, createAuthor, listAuthors}
import scalavitesqlc.pg.mod.{Client, ClientConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.{AggregateError, JavaScriptException}
import scala.util.Random

@main
def main(): Unit = {
  val client = scalavitesqlc.pg.mod.Client(ClientConfig()
    .setPort(53841)
    .setHost("localhost")
    .setUser("user")
    .setPassword("password")
    .setDatabase("dev")
  )
  val f = for {
    _ <- client.connect().toFuture
    sqlcClient = client.asInstanceOf[scalavitesqlc.database.generatedAuthorsSqlMod.Client]
    _ <- createAuthor(sqlcClient, CreateAuthorArgs(name = s"iam author ${Random.nextInt()}")).toFuture
    authors <- listAuthors(sqlcClient).toFuture
    _ <- client.end().toFuture
  } yield {
    println("Authors:")
    authors.foreach({ author =>
      println(s"  ${author.id}: ${author.name}")
    })
  }

  f.onComplete {
    case scala.util.Success(_) =>
      println("Success!")
    case scala.util.Failure(e) =>
      client.end()
      e match {
        case e: JavaScriptException =>
          handleJavaScriptException(e)
        case e =>
          println(e.getClass.toString)
          println(s"An error occurred: ${e.getMessage}")
          e.printStackTrace()
      }
  }
}

def handleJavaScriptException(e: Throwable): Unit = {
  val unwrapped = js.special.unwrapFromThrowable(e)
  unwrapped match
    case e: AggregateError =>
      println(s"An AggregateError occurred")
      e.errors.foreach({ error =>
        println(s"  $error")
      })
    case e: JavaScriptException =>
      println(s"An JavaScriptException occurred: ${e.getMessage}")
      e.printStackTrace()
    case _ =>
      println(s"An JavaScriptException occurred: ${e.getMessage}")
      e.printStackTrace()
}
