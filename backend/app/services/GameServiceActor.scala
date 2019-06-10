package services

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import models._
import play.api.Logger
import play.api.libs.json._
import play.api.libs.json.{JsNumber, JsString, JsValue, Json}
import play.api.mvc.Action
import play.mvc.BodyParser

object GameServiceActor {
  def props(out: ActorRef, user: User, actorSystem: ActorSystem) = Props(new GameServiceActor(out, user, actorSystem))
}


class GameServiceActor(out: ActorRef, user: User, actorSystem: ActorSystem) extends Actor {

  var userDropsACard = false
  var cardToShowOnTop: Card = _

  override def receive: Receive = {
    case msg: InEvent => {
      msg.eventType match {
        case "join" => {
          Logger.debug("JOIN EVENT")
          Game.addPlayer(new Player(user.username, List()))
          actorSystem.actorSelection("/user/*/flowActor").tell(InEvent("nbPlayers", Json.obj()), self)
          if(Game.players.length == 2) {
            Game.newMatch() // init the first match
            actorSystem.actorSelection("/user/*/flowActor").tell(InEvent("startGame", Json.obj()), self)
          }
        }
        case "nbPlayers" => {
          out ! new OutEvent("nbPlayers", playerJoined())
        }
        case "startGame" => {
          out ! new OutEvent("startGame", Json.obj())
        }
        case "getCards" => {
          Logger.info("IN GET CARDS")
          val me: Player = Game.getPlayerByUsername(user.username)
          // the player can see his first two cards only in the beginning of the game
          val myCards: List[Int] = if(!Game.hideCards()) List(0, 1) else List()
          // the player cannot see others' cards
          val others: Map[Player, List[Int]] = Game.players.filter(p => p != me).map(p => p -> List()).toMap

          out ! new OutEvent("getCards", getState(me, myCards, others))
        }
        case "cardClick" => {
          Logger.debug("IN CARD CLICK")
          val deck  = (msg.eventContent \ "deck").validate[String].get
          val name  = (msg.eventContent \ "name").validate[String].get
          val index = (msg.eventContent \ "index").validate[String].get

          // update the game state
          if(user.username == Game.curPlayer().name) {
            Logger.debug("CURRENT PLAYER ACTION")
            if(deck == "closed") {
              Logger.debug("CLOSED DECK CLICK")
              if(!Game.cardIsPicked())
                Game.pickCardFromClosedDeck()
            } else if(deck == "opened") {
              Logger.debug("OPENED DECK CLICK")
              if(Game.cardIsPicked())
                Game.dropCardToOpenedDeck()
              else
                Game.pickCardFromOpenedDeck()
            } else if(name == Game.curPlayer().name) {
              if(Game.cardIsPicked())
                Game.replaceCard(index.toInt)
            }
          }

          // the user wants to drop a card to the opened deck while it's not his turn
          if(name == user.username && index != "nope") {
            val p = Game.getPlayerByUsername(name)
            val cardToDrop = p.seeCard(index.toInt)
            userDropsACard = true
            cardToShowOnTop = cardToDrop
            if(cardToDrop.rank == Game.topOfOpenedDeck().rank) {
              p.dropCard(index.toInt)
            } else {
              p.putCard(Deck52.deck.pickCard())
            }
          }


          /* ******* game flow control ****** */
          //if(Game.roundFinished()) // TODO Fix
            //Game.nextRound()
          if(Game.turnFinished()) {
            Logger.debug("TURN FINISHED")
            Game.nextTurn()
          }

          actorSystem.actorSelection("/user/*/flowActor").tell(InEvent("notifyChange", Json.obj()), self)
          out ! new OutEvent("cardClick", Json.obj())

        }
        case "dropCardToOpenedDeck" => {
          // update the game state
          Game.dropCardToOpenedDeck()

          // the hand should be empty now
          val me: Player = Game.getPlayerByUsername(user.username)
          actorSystem.actorSelection("/user/*/flowActor").tell(InEvent("notifyChange", Json.obj()), self)
          out ! new OutEvent("dropCardToOpenedDeck", Json.obj())
        }
        case "replaceCard" => {
          // TODO: replace the index of the card to replace with one passed in the request
          Game.replaceCard(0)
          val me: Player = Game.getPlayerByUsername(user.username)
          actorSystem.actorSelection("/user/*/flowActor").tell(InEvent("notifyChange", Json.obj()), self)
          out ! new OutEvent("replaceCard", Json.obj())
        }
        case "notifyChange" => {
          out ! new OutEvent("notifyChange", Json.obj())
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
      if(userDropsACard)
        userDropsACard = false
        cardToShowOnTop.toJson
      if (Game.getHand() == null)
        new Card(Rank.empty, Suit.empty, -1).toJson
      else if (Game.getPlayerByUsername(me.name) == Game.getPlayerByUsername(Game.curPlayer().name))
        Game.getHand().toJson
      else new Card(Rank.closed, Suit.closed, -1).toJson
    },
    "others" -> Json.toJson(
      for( (k, v) <- others ) yield k.toJson(v)
    ),
    "openedDeck" -> { if (Game.topOfOpenedDeck() == null) new Card(Rank.empty, Suit.empty, -1).toJson else Game.topOfOpenedDeck().toJson },
    "curPlayer" -> Game.curPlayer().toJson(List()) // do not show the current player's cards
  )
}