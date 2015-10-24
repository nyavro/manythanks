package com.nyavro.manythanks.ws.auth

import spray.json.DefaultJsonProtocol

trait TokenFormat extends DefaultJsonProtocol {
  implicit val tokensFormat = jsonFormat3(Token)
}
