import java.util.ServiceLoader

import com.google.inject.{AbstractModule, Guice, Module}
import play.api.GlobalSettings

import scala.collection.JavaConversions._

object Global extends GlobalSettings {
//
//  val runtimeModules = ServiceLoader.load(classOf[Module]).iterator.toList
//
//  val injector = Guice.createInjector(
//    new AbstractModule {
//      protected def configure() = {
//        bind(classOf[UUIDGenerator]).to(classOf[SimpleUUIDGenerator])
//      }
//    } :: runtimeModules
//  )
//
//  override def getControllerInstance[A](controllerClass: Class[A]) = injector.getInstance(controllerClass)
}
