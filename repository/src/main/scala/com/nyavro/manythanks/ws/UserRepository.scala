package com.nyavro.manythanks.ws

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object UserRepository extends UserRepository

trait UserRepository extends UserTable {

  import driver.api._

  def load(id:Long): Future[Option[UserEntity]] = db.run(user.filter(_.id === id).result.headOption)

  def create(newUser:UserEntity): Future[UserEntity] =
    db.run(
      (user returning user.map(_.id) into ((newEntity, id) => newEntity.copy(id = id))) += newUser
    )

  def update(id: Long, userEntity: UserEntityUpdate): Future[Option[UserEntity]] = load(id).flatMap { loadedOp =>
    val res = for(
        loaded <- loadedOp;
        merged <- userEntity.merge(loaded)
      ) yield {
        db.run(user.filter(_.id === id).update(merged)).map(_ => Some(merged))
      }
    res.getOrElse(Future.successful(None))
  }

  def delete(id: Long): Future[Int] = db.run(user.filter(_.id === id).delete)
}
