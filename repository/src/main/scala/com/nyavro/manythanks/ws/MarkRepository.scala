package com.nyavro.manythanks.ws

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MarkRepository extends MarkRepository

trait MarkRepository extends MarkTable {

  import driver.api._

  def load(id:Long): Future[Option[MarkEntity]] = db.run(mark.filter(_.id === id).result.headOption)

  def marksTo(to:Long): Future[Seq[MarkEntity]] = db.run(mark.filter(_.target === to).result)

  def marksFrom(from:Long): Future[Seq[MarkEntity]] = db.run(mark.filter(_.source === from).result)

  def create(newMark:MarkEntity): Future[MarkEntity] =
    db.run(
      (mark returning mark.map(_.id) into ((newMark, id) => newMark.copy(id = id))) += newMark
    )

  def update(id: Long, markEntity: MarkEntityUpdate): Future[Option[MarkEntity]] = load(id).flatMap { loadedOp =>
    val res = for(
        loaded <- loadedOp;
        merged <- markEntity.merge(loaded)
      ) yield {
        db.run(mark.filter(_.id === id).update(merged)).map(_ => Some(merged))
      }
    res.getOrElse(Future.successful(None))
  }

  def delete(id: Long): Future[Int] = db.run(mark.filter(_.id === id).delete)
}
