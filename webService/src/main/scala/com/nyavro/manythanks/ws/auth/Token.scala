package com.nyavro.manythanks.ws.auth

import java.util.UUID

case class Token(id: Option[Long] = None, userId: Option[Long], token: String = UUID.randomUUID().toString.replaceAll("-", ""))