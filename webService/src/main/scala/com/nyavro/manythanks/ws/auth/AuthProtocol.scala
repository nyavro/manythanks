package com.nyavro.manythanks.ws.auth

import spray.json.DefaultJsonProtocol

trait AuthProtocol extends DefaultJsonProtocol {
  implicit val tokensFormat = jsonFormat3(Token)
}
