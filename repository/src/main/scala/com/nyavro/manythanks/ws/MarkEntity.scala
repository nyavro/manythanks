package com.nyavro.manythanks.ws

import java.util.Date

case class MarkEntity(id: Option[Long] = None, from: Long, to: Long, message:String, time:Long, value:Int)

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
