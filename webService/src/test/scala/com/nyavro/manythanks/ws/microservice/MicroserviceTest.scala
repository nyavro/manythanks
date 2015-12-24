package com.nyavro.manythanks.ws.microservice

import javax.inject.Inject

import com.google.inject.{AbstractModule, Guice}
import org.scalatest.{Matchers, WordSpec}

class MicroserviceTest extends WordSpec with Matchers {
  "Guice" should {
    "inject dependencies" in {
      val injector = Guice.createInjector(
        new AbstractModule() {
          protected def configure() = {
            bind(classOf[ServiceAPI]).to(classOf[ServiceImpl])
          }
        }
      )
      injector.getInstance(classOf[MockService]).get() should === ("service-impl")
    }
    "dependencies inject" in {
      val injector = Guice.createInjector(
        new AbstractModule() {
          protected def configure() = {
            bind(classOf[ServiceAPI]).to(classOf[ImplService])
          }
        }
      )
      injector.getInstance(classOf[MockService]).get() should === ("impl-service")
    }
    "bind string" in {
      val injector = Guice.createInjector(
        new AbstractModule() {
          protected def configure() = {
            bind(classOf[String]).toInstance("str-value")
          }
        }
      )
      injector.getInstance(classOf[String]) should === ("str-value")
    }
  }
}

private trait ServiceAPI {
  def name():String
}

private class ServiceImpl extends ServiceAPI {
  override def name() = "service-impl"
}

private class ImplService extends ServiceAPI {
  override def name() = "impl-service"
}

private class MockService @Inject() (val service:ServiceAPI) {
  def get() = service.name()
}

