package com.nyavro.manythanks.ws

case class UserEntity(id: Option[Long] = None, login: String, gcmToken: String, extId:String)

case class UserEntityUpdate(login:Option[String] = None, gcmToken: Option[String] = None, extId: Option[String] = None) {
  def merge(entity: UserEntity) =
    login
      .map(v => entity.copy(login = v))
      .orElse(
        gcmToken
          .map(v => entity.copy(gcmToken = v))
          .orElse(
            extId
              .map(v => entity.copy(extId = v))
              .orElse(
                Some(entity)
              )
          )
      )
}
