package com.nyavro.manythanks.ws.contact

import spray.json.DefaultJsonProtocol

trait ContactFormat extends DefaultJsonProtocol {
  implicit val contactFormat = jsonFormat2(Contact)
}
