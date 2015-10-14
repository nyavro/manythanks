package com.nyavro.manythanks.ws.route

import akka.http.scaladsl.server.Route

trait RouteProvider {
  def route:Route
}
