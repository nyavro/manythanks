package com.eny.manythanks.contact

import play.api.mvc.PathBindable

object Bindable {

  implicit def listPathBindable():PathBindable[List[String]] = new PathBindable[List[String]] {

    val Delimeter = ","

    override def bind(key: String, value: String): Either[String, List[String]] = {
      Right(value.split(Delimeter).toList)
    }

    override def unbind(key: String, value: List[String]): String = value.mkString(Delimeter)
  }
}
