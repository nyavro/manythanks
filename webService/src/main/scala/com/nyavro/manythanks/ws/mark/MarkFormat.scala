package com.nyavro.manythanks.ws.mark

import spray.json.DefaultJsonProtocol

trait MarkFormat extends DefaultJsonProtocol {
  implicit val format = jsonFormat6(Mark)
}
