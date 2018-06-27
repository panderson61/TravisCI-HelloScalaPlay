package models

import play.api.db._
import play.api.Play.current

//import anorm._
//import anorm.SqlParser._

case class User(
  username: String,
  password: String,
  email: String
)

object User {
  def authenticate(username: String, password: String): Option[User] = {
    findByUsername(username)
    matchPassword(password)
  }

  def findByUsername(username: String): Option[User] = {
    username match {
      case "foo" => Some(User("foo", "foo", "user@foo.com"))
      case _ => None
    }
  }

  def findByEmail(email: String): Boolean = {
    email == "user@foo.com"
  }

  def matchPassword(password: String): Option[User] = {
    password match {
      case "foo" => Some(User("foo", "foo", "user@foo.com"))
      case _ => None
    }
  }
  //def matchPassword(password: String) : Boolean = {
  //  password == "foo"
  //}
//
//  /**
//    * Parse a User from a ResultSet
//    */
//  val simple = {
//    get[String]("user.email") ~
//    get[String]("user.name") ~
//    get[String]("user.password") map {
//      case email ~ name ~ password => User(email, name, password)
//    }
//  }
//
//  /**
//    * Retrieve a User from email.
//    */
//  def findByEmail(email: String): Option[User] = {
//    DB.withConnection { implicit connection =>
//      SQL("select * from user where email = {email}").on(
//        'email -> email).as(User.simple.singleOpt)
//    }
//  }
//
//  /**
//    * Retrieve all users.
//    */
//  def findAll: Seq[User] = {
//    DB.withConnection { implicit connection =>
//      SQL("select * from user").as(User.simple *)
//    }
//  }
//
//  /**
//    * Authenticate a User.
//    */
//  def authenticate(email: String, password: String): Option[User] = {
//    DB.withConnection { implicit connection =>
//      SQL(
//        """
//         select * from user where
//         email = {email} and password = {password}
//        """).on(
//        'email -> email,
//        'password -> password).as(User.simple.singleOpt)
//    }
//  }
//
//  /**
//    * Create a User.
//    */
//  def create(user: User): User = {
//    DB.withConnection { implicit connection =>
//      SQL(
//        """
//          insert into user values (
//            {email}, {name}, {password}
//          )
//        """).on(
//        'email -> user.email,
//        'name -> user.name,
//        'password -> user.password).executeUpdate()
//
//      user
//
//    }
//  }
}