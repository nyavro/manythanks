package com.nyavro.manythanks.ws.contact

import spray.json.DefaultJsonProtocol
import spray.json.BasicFormats

trait ContactFormat extends DefaultJsonProtocol with BasicFormats {
  implicit val contactFormat = jsonFormat2(Contact)
}
