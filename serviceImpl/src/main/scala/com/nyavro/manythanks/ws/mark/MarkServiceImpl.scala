package com.nyavro.manythanks.ws.mark

import com.nyavro.manythanks.gcm.Transport
import com.nyavro.manythanks.ws.{MarkEntity, MarkRepository, UserRepository}
import scala.concurrent.ExecutionContext.Implicits.global

class MarkServiceImpl(transport:Transport, markRepository:MarkRepository, userRepository: UserRepository) extends MarkService {

  val DefaultUp = 10
  val DefaultDown = -5

  override def create(mark: Mark, from:Long) = {
    for (
      entity <- markRepository.create(toEntity(mark, from)).map(fromEntity);
      target <- userRepository.load(mark.to)
    ) yield {
      target.foreach(item => transport.send(mark.message, item.gcmToken))
      Some(entity)
    }
  }

  def toEntity(mark:Mark, from:Long) = MarkEntity(
    None,
    from,
    mark.to,
    mark.message,
    System.currentTimeMillis(),
    if(mark.up) DefaultUp else DefaultDown
  )

  def fromEntity(markEntity: MarkEntity) = Mark(markEntity.to, markEntity.value>0, markEntity.message)
}
