package models

import play.api.db._
import play.api.Play.current

case class User(
  username: String,
  password: String,
  email: String,
  countryCode: String,
  phoneNumber: String,
  authyId: String,
  useAuthy: String
)

object User {

  def create(user: User): Boolean = {
//    val authyUser = User("panderson", "foo", "panderson61@yahoo.com", "1", "7146146687", "38131306", "123")
//    val user.username = "panderson"
    //
    val myUnit = UserList.upsertUser(user.username, user)

    //myUnit.isDefined
    true
  }

  def authenticate(username: String, password: String): Option[User] = {
    val myOptionUser = findByUsername(username)
    // TODO fix ugliness
    val myUser = myOptionUser.getOrElse(User("","","","","","",""))
    println("found user with:" + myUser.phoneNumber)
    password match {
      case myUser.password => Some(myUser)
      case _ => None
    }
//    }
  }

  def findByUsername(username: String): Option[User] = {
    UserList.findUser(username)
  }

//  def findByEmail(email: String): Boolean = {
//    email == "user@foo.com"
//  }
//
//  def matchPassword(password: String): Option[User] = {
//    password match {
//      case "foo" => Some(User("panderson", "foo", "panderson61@yahoo.com", "1", "7146146687", "38131306", "123"))
//      case _ => None
//    }
//  }
//
//  def verifyToken(username: String, token: String): Option[User] = {
//    findByUsername(username)
//    matchToken(token)
//  }
//
//  def matchToken(token: String): Option[User] = {
//    val mySet = UserList.listUsers()
//    mySet.foreach(username => {
//      token match {
//        case "123" => Some(User("panderson", "foo", "panderson61@yahoo.com", "1", "7146146687", "38131306", "123"))
//        case _ => None
//      }
//      )
//  }
//
//  def matchCC(country_code: String): Option[User] = {
//    country_code match {
//      case "1" => Some(User("panderson", "foo", "panderson61@yahoo.com", "1", "7146146687", "38131306", "123"))
//      case _ => None
//    }
//  }

//  def sessionToken(username: String): String = {
//    TODO handle None
//    val myUser = User.findByUsername(username)
//    val myStr = myUser.get.username + myUser.get.password + "ExTr@$tuFF"
//    val sessiontoken = md5HashString(myStr)
//    sessiontoken
//  }
//
//  def md5HashString(s: String): String = {
//    import java.security.MessageDigest
//    import java.math.BigInteger
//    val md = MessageDigest.getInstance("MD5")
//    val digest = md.digest(s.getBytes)
//    val bigInt = new BigInteger(1,digest)
//    val hashedString = bigInt.toString(16)
//    hashedString
//  }

  case object UserList {
  //class UserList {
    val userList = new scala.collection.mutable.HashMap[String, User]()

    def upsertUser(username: String, user: User): Unit = {
      println("upsertUser: " + username)
      userList.update(username, user)
    }

    def findUser(username: String): Option[User] = {
      userList.foreach {
        case(username, user) => println("look for user:" + username)

      }
      userList.get(username)
    }

//    def listUsers(): Set = {
//      userList.keySet
//    }
  }

//    def matchToken(myUsername: String, mySessionToken: String): Option[User] = {
//      userList.foreach {
//        case(localUsername, localUser) =>
//          User.sessionToken(localUsername) match  {
//            case mySessionToken => Some(localUser)
//            case _ => None
//          }
//        case _ => None
//      }
//    }
//  }
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