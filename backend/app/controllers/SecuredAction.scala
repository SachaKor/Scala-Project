package controllers

import javax.inject.Inject
import models.DataSource
import play.api.libs.json.Json
import play.api.mvc._
import play.api.mvc.Results._
import utilities.JwtUtility

import scala.concurrent.{ExecutionContext, Future}

case class UserInfo(id: Int,
                    firstName: String,
                    lastName: String,
                    email: String)

case class User(email: String, userId: String)

case class UserRequest[A](userInfo: UserInfo, request: Request[A]) extends WrappedRequest[A](request)

class SecuredAction @Inject()(val parser: BodyParsers.Default, dataSource: DataSource)(implicit val executionContext: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] with ActionRefiner[Request, UserRequest] {
  implicit val formatUserDetails = Json.format[User]

  override def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = Future.successful {
    val jwtToken = request.headers.get("Authorization").getOrElse("")

    if (JwtUtility.isValidToken(jwtToken)) {
      JwtUtility.decodePayload(jwtToken) match {
        case Some(payload) => {
          val userCredentials = Json.parse(payload).validate[User].get

          // Replace this block with data source
          val maybeUserInfo = dataSource.getUser(userCredentials.email, userCredentials.userId)
          maybeUserInfo.map(userInfo => UserRequest(userInfo, request)).toRight(Unauthorized("Invalid credentials"))
        }
        case None => Left(Unauthorized("Invalid credential"))
      }
    } else {
      Left(Unauthorized("Invalid credential"))
    }
  }
}