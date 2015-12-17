package com.nyavro.manythanks.ws.user

import scala.concurrent.Future

trait UserService {
  def create(user:User):Future[Option[User]]
}
