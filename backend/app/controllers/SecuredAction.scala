package controllers

import models.User
import models.Login
import dao.UserDAO
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json._
import javax.inject.Inject

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import utilities.JwtUtility
import play.api.Logger

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest[A](request)

class SecuredAction @Inject()(val parser: BodyParsers.Default, userDAO: UserDAO)(implicit val executionContext: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] with ActionRefiner[Request, UserRequest] {
  implicit val formatUserDetails = Json.format[Login]

  override def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = Future.successful {
    val jwtToken = request.headers.get("Authorization").getOrElse("")

    if (JwtUtility.isValidToken(jwtToken)) {
      JwtUtility.decodePayload(jwtToken) match {
        case Some(payload) => {
          Logger.debug(payload)

          val credentials = Json.parse(payload).validate[Login].get
          val findUser = userDAO.findByUsernameAndPassword(credentials.username, credentials.password)
          val u = Await.result(findUser, 1 second)

          u match {
            case Some(u) =>
              Right(UserRequest(u, request))
            case None =>
              Left(Unauthorized("Invalid credentials"))
          }
        }
        case None => Left(Unauthorized("Invalid credential"))
      }
    } else {
      Left(Unauthorized("Invalid credential"))
    }
  }
}