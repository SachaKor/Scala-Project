package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Singleton
import javax.inject.Inject
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AbstractController, ControllerComponents, WebSocket}
import services.GameServiceActor
import play.api.Logger
import play.api.libs.json._
import utilities.JwtUtility
import models.Login
import dao.UserDAO

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._




/**
  * Game Controller instance that handles the incoming game requests
  *
  */
@Singleton
class GameController @Inject()(cc: ControllerComponents, userDAO: UserDAO)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  def socket = WebSocket.acceptOrResult[String, String] { request =>
    Logger.debug("In socket")
    Logger.debug(request.toString())

    implicit val formatUserDetails = Json.format[Login]

    Future.successful(request.headers.get("Sec-WebSocket-Protocol") match {
      case None => Left(Forbidden)
      case Some(jwtToken) =>
        Logger.debug(jwtToken)
        if (JwtUtility.isValidToken(jwtToken)) {
          JwtUtility.decodePayload(jwtToken) match {
            case Some(payload) => {
              Logger.debug(payload)

              val credentials = Json.parse(payload).validate[Login].get
              val findUser = userDAO.findByUsernameAndPassword(credentials.username, credentials.password)
              val u = Await.result(findUser, 1 second)

              u match {
                case Some(user) => {
                  Right(
                    ActorFlow.actorRef { out =>
                      GameServiceActor.props(out, user)
                    })
                }
                case None =>
                  Left(Unauthorized("Invalid credentials"))
              }
            }
          }
        } else {
          Left(Forbidden)
        }
    })
  }
}