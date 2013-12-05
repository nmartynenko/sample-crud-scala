package com.aimprosoft.scala

import org.eclipse.jetty.server.{Server, ServerConnector}
import org.eclipse.jetty.webapp.WebAppContext

object JettyMain extends App {

  val server = new Server

  val connector = new ServerConnector(server)
  connector.setPort(8080)
  server.addConnector(connector)

  val root = new WebAppContext("src/main/webapp", "/")
  server.setHandler(root)

  server.start()
  server.join()
}
