package com.nyavro.manythanks.ws.mark

import scala.concurrent.Future

trait MarkService {
  def create(mark:Mark, from:Long):Future[Option[Mark]]
}
