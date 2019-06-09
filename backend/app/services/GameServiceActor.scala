package services

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import models._
import play.api.Logger

object GameServiceActor {
  def props(out: ActorRef, user: User) = Props(new GameServiceActor(out, user))
}

class GameServiceActor(out: ActorRef, user: User) extends Actor {
  override def receive: Receive = {
    case msg: InEvent => {
      msg.eventType match {
        case "join" => {
          Logger.debug("JOIN EVENT")
          out ! new OutEvent("Hello")
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