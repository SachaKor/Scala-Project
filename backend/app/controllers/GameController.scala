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

  def socket = WebSocket.accept[String, String] { request =>
    Logger.info("In socket")
    ActorFlow.actorRef { out =>
      GameServiceActor.props(out)
    }
  }
}