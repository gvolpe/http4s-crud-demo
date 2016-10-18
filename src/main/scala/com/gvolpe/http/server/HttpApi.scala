package com.gvolpe.http.server

import com.gvolpe.http.server.endpoint.UserHttpEndpoint
import org.http4s.server.{Server, ServerApp}
import org.http4s.server.blaze._

import scalaz.concurrent.Task

object HttpApi extends ServerApp {

  override def server(args: List[String]): Task[Server] = {
    BlazeBuilder
      .bindHttp(8080, "localhost")
      .mountService(UserHttpEndpoint.service)
      .start
  }

}
