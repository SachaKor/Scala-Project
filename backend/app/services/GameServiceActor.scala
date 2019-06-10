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
          actorSystem.actorSelection("/user/*/flowActor").tell(InEvent("nbPlayers"), self)
          if(Game.players.length == 2) {
            actorSystem.actorSelection("/user/*/flowActor").tell(InEvent("startGame"), self)
          }
          Game.newMatch() // init the first game
        }
        case "nbPlayers" => {
          out ! new OutEvent("nbPlayers", playerJoined())
        }
        case "startGame" => {
          out ! new OutEvent("startGame", Json.obj())
        }
        case "getCards" => {
          Logger.info("In getCards")
          val me: Player = Game.getPlayerByUsername(user.username)
          val myCards: List[Int] = List(0, 1) // the player can see his first two cards
          // the player cannot see others' cards
          val others: Map[Player, List[Int]] = Game.players.filter(p => p != me).map(p => p -> List()).toMap
          Logger.debug(getState(me, myCards, others).toString())
          Logger.debug(others.toString)

          out ! new OutEvent("getCards", getState(me, myCards, others))
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