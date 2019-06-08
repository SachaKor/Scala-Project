package services

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import models.{Game, Player, User}
import play.api.Logger
import play.api.libs.json._

object GameServiceActor {
  def props(out: ActorRef, user: User) = Props(new GameServiceActor(out, user))
}

class GameServiceActor(out: ActorRef, user: User) extends Actor {
  override def receive: Receive = {
    case msg: JsValue =>
      (msg \ "type").validate[String] match {
        case JsSuccess(value, _) =>
          value match {
            case "join" =>
              Game.addPlayer(new Player(user.username, List()))
              out ! Json.obj(
                "status" -> "OK",
                "message" -> s"Player ${user.username} has been added to the game"
              )
          }
        case e: JsError =>
          Logger.error("Unable to deserialize JSON message")
          out ! Json.obj(
            "status" -> "ERROR"
          )
          self ! PoisonPill
      }
  }

  override def postStop() {
    println("Closing the websocket connection.")
  }
}