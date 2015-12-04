package com.nyavro.manythanks.ws

import com.typesafe.config.ConfigFactory

trait DatabaseConfig {

  val driver = slick.driver.MySQLDriver

  import driver.api._

  def db = Database.forConfig("database")

  implicit val session: Session = db.createSession()

  private val config = ConfigFactory.load()
  val databaseUrl = config.getString("url")
  val databaseUser = config.getString("user")
  val databasePassword = config.getString("password")
}
