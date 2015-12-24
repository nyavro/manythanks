package com.nyavro.manythanks.ws.microservice

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val http = config.getConfig("http")

  val httpInterface = http.getString("interface")
  val httpPort = http.getInt("port")
}
