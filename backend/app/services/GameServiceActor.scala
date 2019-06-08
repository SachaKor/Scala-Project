package services

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import models.{Game, User}

object GameServiceActor {
  def props(out: ActorRef, user: User) = Props(new GameServiceActor(out, user))
}

class GameServiceActor(out: ActorRef, user: User) extends Actor {
  override def receive: Receive = {
    case msg: String if msg.contains("close") =>
      out ! s"Closing the connection as requested"
      self ! PoisonPill
    case msg: String =>
      msg match {
        case "JOIN" => {

          out ! s"Echo, Received the message: ${msg}"
        }
        case _ => out ! s"Echo, Received the message: ${msg}"
      }
  }

  override def postStop() {
    println("Closing the websocket connection.")
  }
}