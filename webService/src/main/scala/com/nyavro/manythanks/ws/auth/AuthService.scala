package com.nyavro.manythanks.ws.auth

import com.nyavro.manythanks.ws.user.User

import scala.concurrent.Future

trait AuthService {

  /**
   * Authenticate user by token
   * @param token token previously retrieved with call to signUp or signIn
   * @return Some(User)
   */
  def authenticate(token: String): Future[Option[User]]

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
