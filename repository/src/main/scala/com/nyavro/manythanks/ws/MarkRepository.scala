package com.nyavro.manythanks.ws

trait MarkRepository extends DatabaseConfig {

  import driver.api._

  class Mark(tag: Tag) extends Table[MarkEntity](tag, "mark") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def source = column[Long]("source")
    def target = column[Long]("target")
    def message = column[String]("message")
    def time = column[Long]("tm")
    def value = column[Int]("value")

    def * = (id, source, target, message, time, value) <> ((MarkEntity.apply _).tupled, MarkEntity.unapply)
  }

  protected val mark = TableQuery[Mark]

}
