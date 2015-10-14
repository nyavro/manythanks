package com.nyavro.manythanks.ws.mark

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, MediaTypes, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.nyavro.manythanks.ws.Protocols
import com.nyavro.manythanks.ws.auth.AuthService
import com.nyavro.manythanks.ws.security.Directives
import com.nyavro.manythanks.ws.user.User
import org.scalatest.{Matchers, WordSpec}
import spray.json.{JsObject, _}
import spray.routing.HttpService
import scala.concurrent.Future

class MarkRouteTest extends WordSpec with Matchers with ScalatestRouteTest with MarkFormat with Protocols {
  "Mark route" should {
    "Create mark on authorized request" in {
      val newMark = Mark(None, "26-10-30", 35L, 51L, true, "Good luck!")
      val markRoute = new MarkRoute(
        new MarkService {
          override def create(mark:Mark) = Future(Some(mark.copy(id=Some(3L))))
        },
        new Directives(
          new AuthService {
            override def authenticate(token: String) = Future(Some(User(Some(1), "32", "en", "pwd")))
            override def signUp(user: User) = ???
            override def signIn(login: String, password: String) = ???
          }
        )
      )
      Post(
        "/mark/create",
        HttpEntity(MediaTypes.`application/json`, newMark.toJson.toString())
      ) ~> addHeader("Token", "dummyTokenValue") ~> markRoute.route ~> check {
        responseAs[JsObject] should be(newMark.copy(id = Some(3L)).toJson)
        response.status should be(StatusCodes.Created)
      }
    }
    "Reject mark creation request on invalid token" in {
      val newMark = Mark(None, "26-10-30", 35L, 51L, true, "Good luck!")
      val markRoute = new MarkRoute(
        new MarkService {
          override def create(mark:Mark) = Future(None)
        },
        new Directives(
          new AuthService {
            override def authenticate(token: String) = Future(
              token match {
                case "correctToken" => Some(User(Some(1), "32", "en", "pwd"))
                case _ => None
              }
            )
            override def signUp(user: User) = ???
            override def signIn(login: String, password: String) = ???
          }
        )
      )
      Post(
        "/mark/create",
        HttpEntity(MediaTypes.`application/json`, newMark.toJson.toString())
      ) ~> addHeader("Token", "invalidToken") ~> markRoute.route ~> check {
        handled shouldBe false
      }
    }
    "Reject mark creation request on missing token" in {
      val newMark = Mark(None, "26-10-31", 75L, 61L, true, "Good luck2!")
      val markRoute = new MarkRoute(
        new MarkService {
          override def create(mark:Mark) = Future(None)
        },
        new Directives(
          new AuthService {
            override def authenticate(token: String) = Future(
              token match {
                case "correctToken" => Some(User(Some(1), "321", "end", "pwd"))
                case _ => None
              }
            )
            override def signUp(user: User) = ???
            override def signIn(login: String, password: String) = ???
          }
        )
      )
      Post(
        "/mark/create",
        HttpEntity(MediaTypes.`application/json`, newMark.toJson.toString())
      ) ~> markRoute.route ~> check {
        handled shouldBe false
      }
    }
  }
}

