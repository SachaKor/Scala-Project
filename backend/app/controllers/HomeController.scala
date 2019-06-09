package controllers

import dao.UserDAO
import models.User
import models.Login
import utilities.JwtUtility
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, userDAO: UserDAO, securedAction: SecuredAction) extends AbstractController(cc) {

  // Convert a User-model object into a JsValue representation, which means that we serialize it into JSON.
  implicit val userToJson: Writes[User] = (
      (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "username").write[String] and
      (JsPath \ "password").write[String]
    // Use the default 'unapply' method (which acts like a reverted constructor) of the User case class if order to get
    // back the User object's arguments and pass them to the JsValue.
    )(unlift(User.unapply))

  // Convert a JsValue representation into a User-model object, which means that we deserialize the JSON.
  implicit val jsonToUser: Reads[User] = (
      (JsPath \ "id").readNullable[Long] and
      (JsPath \ "username").read[String](minLength[String](2)) and
      (JsPath \ "password").read[String](minLength[String](2))
    // Use the default 'apply' method (which acts like a constructor) of the User case class with the JsValue in order
    // to construct a User object from it.
    )(User.apply _)

  implicit val jsonToLogin: Reads[Login] = (
      (JsPath \ "username").read[String](minLength[String](2)) and
      (JsPath \ "password").read[String](minLength[String](2))
    // Use the default 'apply' method (which acts like a constructor) of the User case class with the JsValue in order
    // to construct a User object from it.
    )(Login.apply _)

  /**
    * This helper parses and validates JSON using the implicit `jsonToUser` above, returning errors if the parsed
    * json fails validation.
    */
  def validateJson[A : Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def index = Action {
    Ok(
      Json.obj(
      "status"  -> "OK",
      "message" -> "Welcome to the Youglouf API"
    ))
  }

  def secure = securedAction.async { implicit request => {
      Future.successful(
        Ok(
          Json.obj(
            "status" -> "OK",
            "message" -> s"You are VIP ${request.user.username}"
          )
        )
      )
    }
  }


  def login = Action.async(validateJson[Login]) { implicit request =>

    val credentials = request.body
    val findUser = userDAO.findByUsernameAndPassword(credentials.username, credentials.password)

    findUser.map {
      case Some(u) => Ok(
        Json.obj(
          "status" -> "OK",
          "user" -> Json.obj(
            "username" -> u.username,
            "id" -> u.id,
            "token" -> JwtUtility.createToken(
              Json.obj(
                "username" -> u.username,
                "password" -> u.password,
              ).toString
            )
          )
        )
      )
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the student does not exist.
        Forbidden("Wrong username or password !")
    }
  }

  def signup = Action.async(validateJson[User]) { implicit request =>
    // `request.body` contains a fully validated `User` instance, since it has been validated by the `validateJson`
    // helper above.
    val user = request.body
    val createdUser = userDAO.insert(user)

    createdUser.map(u =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "user" -> Json.obj("id" -> u.id, "username" -> u.username),
          "message" -> ("User '" + u.username + "' created.")
        )
      )
    )
  }
}
