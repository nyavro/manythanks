package com.nyavro.manythanks.ws.contact

import scala.concurrent.Future

trait ContactService {
  def list(extIds: List[String]):Future[List[Contact]]

}
