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

import scala.concurrent.ExecutionContextExecutor


class MarkRoute(service:MarkService, directives:Directives)
  (implicit private val system:ActorSystem,
   implicit private val executor: ExecutionContextExecutor,
   implicit private val materializer: ActorMaterializer) extends RouteProvider with SprayJsonSupport with MarkFormat {

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
