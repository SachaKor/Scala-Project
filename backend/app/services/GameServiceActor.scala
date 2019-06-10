package services

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import models._
import play.api.Logger
import play.api.libs.json.{JsNumber, JsValue, Json}
import play.api.mvc.Action

object GameServiceActor {
  def props(out: ActorRef, user: User, actorSystem: ActorSystem) = Props(new GameServiceActor(out, user, actorSystem))
}


class GameServiceActor(out: ActorRef, user: User, actorSystem: ActorSystem) extends Actor {

  override def receive: Receive = {
    case msg: InEvent => {
      msg.eventType match {
        case "join" => {
          Logger.debug("JOIN EVENT")
          Game.addPlayer(new Player(user.username, List()))
          actorSystem.actorSelection("/user/*/flowActor").tell(new InEvent("nbPlayers"), self)
        }
        case "nbPlayers" => {
          out ! new OutEvent("nbPlayers", playerJoined())
        }
        case "leave" => {
          self ! PoisonPill
        }
      }
    }
    case _ =>
      Logger.debug("ERROR")
      out ! new OutEvent("Error", Json.obj())

  }

  override def postStop() {
    Logger.info("Closing the websocket connection.")
  }

  def playerJoined(): JsValue = Json.obj(
    "nbPlayers" -> Game.players.length
  )

  def getState(me: Player, myCards: List[Int], others: Map[Player, List[Int]]): JsValue = Json.obj(
    "me" -> me.toJson(myCards),
    "others" -> Json.toJson(
      for( (k, v) <- others ) yield k.toJson(v)
    ),
    "openedDeck" -> Game.topOfOpenedDeck().toJson,
    "curPlayer" -> Game.curPlayer().toJson(List()) // do not show the current player's cards
  )
}