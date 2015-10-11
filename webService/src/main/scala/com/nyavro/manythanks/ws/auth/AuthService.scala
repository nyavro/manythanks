package com.nyavro.manythanks.ws.auth

import com.nyavro.manythanks.user.User

import scala.concurrent.Future

trait AuthService {
  def signUp(user: User):Future[Option[Token]]

}
