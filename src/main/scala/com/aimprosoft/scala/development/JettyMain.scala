package com.aimprosoft.scala.development

import org.eclipse.jetty.server.{Server, ServerConnector}
import org.eclipse.jetty.webapp.WebAppContext

//this class is intended to be for development purposes
//in general project needs to be packaged as WAR file
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
