package com.nyavro.manythanks.ws

trait MarkRepository extends DatabaseConfig {

  import driver.api._

  class Mark(tag: Tag) extends Table[MarkEntity](tag, "mark") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def from = column[Long]("from")
    def to = column[Long]("to")
    def message = column[String]("message")
    def time = column[Long]("tm")
    def value = column[Int]("value")

    def * = (id, from, to, message, time, value) <> ((MarkEntity.apply _).tupled, MarkEntity.unapply)
  }

  protected val mark = TableQuery[Mark]

}
