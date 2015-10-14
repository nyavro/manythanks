package com.nyavro.manythanks.ws.mark

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.nyavro.manythanks.ws.route.RouteProvider
import com.nyavro.manythanks.ws.security.Directives
import spray.json._


class MarkRoute(service:MarkService, directives:Directives) extends RouteProvider with SprayJsonSupport with MarkFormat {

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  override def route: Route = pathPrefix("mark") {
    path("create") {
      pathEndOrSingleSlash {
        directives.authenticate { loggedUser =>
          post {
            entity(as[Mark]) { mark =>
              complete(Created -> service.create(mark).map(_.toJson))
            }
          }
        }
      }
    } ~ path("test") {
      get {
        println("here")
        complete(OK)
      }
    }
  }
}