package com.nyavro.manythanks.ws

case class MarkEntity(id: Option[Long] = None, from: Long, to: Long, message:String, ts:Long, value:Int)

case class MarkEntityUpdate(msg: Option[String] = None, vl: Option[Int] = None) {
  def merge(mark: MarkEntity) =
    vl
      .map(v => mark.copy(value = v))
      .orElse(
        msg
          .map(v => mark.copy(message = v))
          .orElse(Some(mark))
      )
}
