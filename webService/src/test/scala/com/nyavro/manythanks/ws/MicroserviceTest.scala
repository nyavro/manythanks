package com.nyavro.manythanks.ws

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.nyavro.manythanks.user.User
import com.nyavro.manythanks.ws.auth.{AuthRoute, AuthService, Token}
import org.scalatest.{Matchers, WordSpec}
import spray.json.{JsValue, _}

import scala.concurrent.Future

class MicroserviceTest extends WordSpec with Matchers with ScalatestRouteTest with Protocols  {

  val newUser = User(Some(3L), "26 10 30", "Mars", "test1")
  val mockToken = Token(Some(123L), Some(1L), "123token321")

  "Authentication service" should {
    "Create user and return user's token" in {
      val authRoute = new AuthRoute(
        new AuthService {
          override def signUp(user: User): Future[Option[Token]] = Future(Some(mockToken))
        }
      )
      Post(
        "/auth/signUp",
        HttpEntity(MediaTypes.`application/json`, newUser.toJson.toString())
      ) ~> authRoute.route ~> check {
        response.status should be(StatusCodes.Created)
        mockToken should be (tokensFormat.read(responseAs[JsValue]))
      }
    }
  }

}

