package com.nyavro.manythanks.ws.user

import spray.json.DefaultJsonProtocol

trait UserFormat extends DefaultJsonProtocol {
  implicit val usersFormat = jsonFormat4(User)
}
