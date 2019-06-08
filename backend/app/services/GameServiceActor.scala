package services

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import models.{Game, Player, User}
import play.api.libs.json.Json

object GameServiceActor {
  def props(out: ActorRef, user: User) = Props(new GameServiceActor(out, user))
}

class GameServiceActor(out: ActorRef, user: User) extends Actor {
  override def receive: Receive = {
    case msg: String =>
      msg match {
        case "JOIN" =>
          Game.addPlayer(new Player(user.username, List()))
          out ! Json.obj(
            "status" -> "OK",
            "message" -> s"Player ${user.username} has been added to the game"
          )
      }
  }

  override def postStop() {
    println("Closing the websocket connection.")
  }
}