package services

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import models._
import play.api.Logger
import play.api.libs.json.{JsNumber, JsValue, Json}

object GameServiceActor {
  def props(out: ActorRef, user: User) = Props(new GameServiceActor(out, user))
}


class GameServiceActor(out: ActorRef, user: User) extends Actor {
  def playerJoined(): JsValue = Json.obj(
    "nb-players:" -> Game.players.length
  )


  override def receive: Receive = {
    case msg: InEvent => {
      msg.eventType match {
        case "join" => {
          Logger.debug("JOIN EVENT")
          Game.addPlayer(new Player(user.username, List()))
          out ! new OutEvent("joined")
        }
        case "leave" => {
          self ! PoisonPill
        }
      }
    }
    case _ =>
      Logger.debug("ERROR")
      out ! new OutEvent("Error")

  }

  override def postStop() {
    Logger.info("Closing the websocket connection.")
  }
}