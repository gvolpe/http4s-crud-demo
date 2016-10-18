package com.gvolpe.http.client

import org.http4s.Uri
import org.http4s.client.blaze._

import scalaz.concurrent.Task

object HttpClient extends App {

  val client = PooledHttp1Client()

  def findById(id: Long) = {
    val target = Uri.uri("http://localhost:8080") / "users" / id.toString
    client.expect[String](target)
  }

  val result = Task.gatherUnordered((1L to 5L).map(findById))

  println(result.run)

  client.shutdownNow()
}
