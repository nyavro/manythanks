package com.nyavro.manythanks.ws

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import scala.concurrent.ExecutionContext.Implicits.global

class MarkRepositoryTest extends WordSpec with Matchers with ScalaFutures with Config {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  "Mark repository" ignore {
    val to = 3L
    "save mark" in {
      whenReady(MarkRepository.create(MarkEntity(None, 1L, to, "hello", System.currentTimeMillis(), 10))) { result =>
        result.id.isDefined should === (true)
      }
      whenReady(MarkRepository.marksTo(to)) { result =>
        result.size !== 0
      }
      whenReady(
        for(
          prev <- MarkRepository.marksTo(to);
          remove <- MarkRepository.delete(prev.head.id.get);
          post <- MarkRepository.marksTo(to)
        ) yield post
      ) { result =>
        result.size === 0
      }
    }
  }
}
