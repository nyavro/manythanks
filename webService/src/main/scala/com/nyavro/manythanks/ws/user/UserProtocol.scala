package com.nyavro.manythanks.ws.user

import spray.json.DefaultJsonProtocol

trait UserProtocol extends DefaultJsonProtocol {
  implicit val usersFormat = jsonFormat4(User)
}
