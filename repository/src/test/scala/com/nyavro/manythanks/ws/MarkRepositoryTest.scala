package com.nyavro.manythanks.ws

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class MarkRepositoryTest extends WordSpec with Matchers with ScalaFutures with Config {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  "Mark repository" ignore {
    "save mark" in {
      whenReady(
        for (
          created <- MarkRepository.create(MarkEntity(None, 1L, 3L, "hello", System.currentTimeMillis(), 10));
          remove <- MarkRepository.delete(created.id.get)
        ) yield created
      ) { result =>
        result.id.isDefined should ===(true)
      }
    }
    "loads marks by target" in {
      val random = new Random()
      val fromA = random.nextLong()
      val fromB = random.nextLong()
      val to = 5L
      whenReady(
        for (
          a <- MarkRepository.create(MarkEntity(None, fromA, to, "hey!", System.currentTimeMillis(), 10));
          b <- MarkRepository.create(MarkEntity(None, fromB, to, "nice!", System.currentTimeMillis(), 10));
          list <- MarkRepository.marksTo(to);
          ra <- MarkRepository.delete(a.id.get);
          rb <- MarkRepository.delete(b.id.get)
        ) yield list
      ) { result =>
        result.size should === (2)
        result.map(item => item.from).toSet should === (Set(fromA, fromB))
        result.map(item => item.message).toSet should === (Set("hey!", "nice!"))
      }
    }
    "deletes marks by id" in {
      whenReady(
        for (
          created <- MarkRepository.create(MarkEntity(None, 127L, 7L, "cool", System.currentTimeMillis(), 10));
          remove <- MarkRepository.delete(created.id.get);
          loaded <- MarkRepository.load(created.id.get)
        ) yield loaded
      ) { result =>
        result.isDefined should === (false)
      }
    }
    "load mark" in {
      val to = 11L
      whenReady(
        for(
          created <- MarkRepository.create(MarkEntity(None, 17L, to, "hello", System.currentTimeMillis(), 10));
          loaded <- MarkRepository.load(created.id.get);
          deleted <- MarkRepository.delete(created.id.get)
        ) yield created
      ) { result =>
        result.id.isDefined should === (true)
      }
    }
    "updates" in {
      whenReady(
        for(
          created <- MarkRepository.create(MarkEntity(None, 19L, 23L, "colnes", System.currentTimeMillis(), 10));
          updated <- MarkRepository.update(created.id.get, MarkEntityUpdate(Some("coolness!!!")));
          deleted <- MarkRepository.delete(created.id.get)
        ) yield (created, updated)
      ) { case (created, updated) =>
        updated.isDefined should === (true)
        created.id should === (updated.get.id)
        updated.get.message should === ("coolness!!!")
      }
    }
  }
}
