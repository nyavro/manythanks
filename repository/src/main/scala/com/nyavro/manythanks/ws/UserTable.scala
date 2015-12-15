package com.nyavro.manythanks.ws

trait UserTable extends DatabaseConfig {

  import driver.api._

  class User(tag: Tag) extends Table[UserEntity](tag, "user") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def login = column[String]("login")
    def gcmToken = column[String]("gcm_token")
    def extId = column[String]("ext_id")
    def * = (id, login, gcmToken, extId) <> ((UserEntity.apply _).tupled, UserEntity.unapply)
  }

  protected val user = TableQuery[User]

}
