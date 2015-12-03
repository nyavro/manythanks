package com.nyavro.manythanks.ws

trait DatabaseConfig {

  val driver = slick.driver.MySQLDriver

  import driver.api._

  def db = Database.forConfig("database")

  implicit val session: Session = db.createSession()
}
