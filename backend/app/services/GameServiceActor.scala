package services

import akka.actor.{Actor, ActorRef, PoisonPill, Props}

object GameServiceActor {
  def props(out: ActorRef) = Props(new GameServiceActor(out))
}

class GameServiceActor(out: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: String if msg.contains("close") =>
      out ! s"Closing the connection as requested"
      self ! PoisonPill
    case msg: String =>
      out ! s"Echo, Received the message: ${msg}"
  }

  override def postStop() {
    println("Closing the websocket connection.")
  }
}