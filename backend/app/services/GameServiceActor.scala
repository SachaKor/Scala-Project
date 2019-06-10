package services

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import models._
import play.api.Logger
import play.api.libs.json
import play.api.libs.json.{JsNumber, JsString, JsValue, Json}
import play.api.mvc.Action
import play.mvc.BodyParser

object GameServiceActor {
  def props(out: ActorRef, user: User, actorSystem: ActorSystem) = Props(new GameServiceActor(out, user, actorSystem))
}


class GameServiceActor(out: ActorRef, user: User, actorSystem: ActorSystem) extends Actor {

  /* ******************** controller flags ************************** */
  var pickedFromOpenedDeck: Boolean = false // current player picked a card from the opened deck

  override def receive: Receive = {
    case msg: InEvent => {
      msg.eventType match {
        case "join" => {
          Logger.debug("JOIN EVENT")
          Game.addPlayer(new Player(user.username, List()))
          actorSystem.actorSelection("/user/*/flowActor").tell(new InEvent("nbPlayers"), self)
          if(Game.players.length == 2) {
            Game.newMatch() // init the first match
            actorSystem.actorSelection("/user/*/flowActor").tell(new InEvent("startGame"), self)
          }
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
          Logger.debug(me.toString)
          val myCards: List[Int] = List(0, 1) // the player can see his first two cards
          Logger.debug(myCards.toString)
          // the player cannot see others' cards
          val others: Map[Player, List[Int]] = Game.players.filter(p => p != me).map(p => p -> List()).toMap
          Logger.debug(others.toString)
          Logger.debug(getState(me, myCards, others).toString())

          out ! new OutEvent("getCards", getState(me, myCards, others))
        }
        case "pickCardFromOpenedDeck" => {
          val card: Card = Game.pickCardFromClosedDeck()
          pickedFromOpenedDeck = true
        }
        case "pickCardFromClosedDeck" => {
          val card: Card = Game.pickCardFromClosedDeck()
          pickedFromOpenedDeck = false
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
    "hand" -> {
      if (Game.getPlayerByUsername(me.name) == Game.getPlayerByUsername(Game.curPlayer().name))
        Game.getHand().toJson
      else new Card(Rank.closed, Suit.closed, -1).toJson
    },
    "others" -> Json.toJson(
      for( (k, v) <- others ) yield k.toJson(v)
    ),
    "openedDeck" -> { if (Game.topOfOpenedDeck() == null) JsString("empty") else Game.topOfOpenedDeck().toJson },
    "curPlayer" -> Game.curPlayer().toJson(List()) // do not show the current player's cards
  )
}