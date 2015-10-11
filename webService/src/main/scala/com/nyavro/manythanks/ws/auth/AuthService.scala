package com.nyavro.manythanks.ws.auth

import com.nyavro.manythanks.user.User

import scala.concurrent.Future

trait AuthService {

  /**
   * User registration request
   * @param user - new user to create and obtain token for
   * @return Some(Token) if succeeded
   */
  def signUp(user: User):Future[Option[Token]]

  /**
   * User sign in request
   * @param login user login
   * @param password user password
   * @return Some(Token) if succeeded
   */
  def signIn(login: String, password: String):Future[Option[Token]]

}
