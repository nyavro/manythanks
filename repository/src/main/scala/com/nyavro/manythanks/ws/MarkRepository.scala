package com.nyavro.manythanks.ws

import scala.concurrent.Future

object MarkRepository extends MarkRepository

trait MarkRepository extends MarkTable {

  import driver.api._

  def marksTo(id:Long): Future[Seq[MarkEntity]] = db.run(mark.filter(_.target === id).result)

  def create(newMark:MarkEntity): Future[MarkEntity] =
    db.run(
      (mark returning mark.map(_.id) into ((newMark, id) => newMark.copy(id = id))) += newMark
    )

  def delete(id: Long): Future[Int] = db.run(mark.filter(_.id === id).delete)
}
