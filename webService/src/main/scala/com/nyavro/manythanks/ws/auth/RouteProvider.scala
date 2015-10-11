package com.nyavro.manythanks.ws.auth

import akka.http.scaladsl.server.Route

trait RouteProvider {
  def route:Route
}
