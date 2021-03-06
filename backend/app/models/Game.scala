package models

import java.util.Random

/**
  * - Initiate the game
  * - Add players to the game
  * - Start the match
  * - Start the round
  */
object Game {
  val MAX_SCORE = 100
  var players: List[Player] = List()
  var matches: List[Match] = List()
  var gameScores: Map[Player, Int] = players.map(p => (p, 0)).toMap
  // the player who starts the match
  private var firstPlayer: Player = _

  /**
    * Finds the next player to play the turn
    * @param players the list of the players in the game
    * @param curPlayer the current player
    * @return the next player to play the turn
    */
  private def nextPlayer(players: List[Player], curPlayer: Player): Player = {
    def loop(ps: List[Player], p: Player): Player = {
      if(ps.head == curPlayer) ps.tail.head
      else loop(ps.tail, p)
    }
    if(players.isEmpty || !players.contains(curPlayer))
      throw new Error("Player " + curPlayer.name + " is not found")
    if(players.last == curPlayer) players.head // last player in the list played => the next is the first player
    else loop(players, curPlayer) // otherwise, find the next Player in the list
  }

  /**
    * Returns the player with a given username
    * @param username the username of the player to return
    * @return the player with a given username if such a player exists, null otherwise
    */
  def getPlayerByUsername(username: String): Player = {
    players.find(p => p.name == username) match {
      case Some(p) => p
      case None => null
    }
  }

  /**
    * TODO: probably limit the number of players
    * Makes the player join the game
    * @param player the player that joins the game
    */
  def addPlayer(player: Player) = {
    if(!players.exists(p => p.name == player.name))
      players = player :: players
  }

  /**
    * Adds the players to the game
    * @param players the list of players to add
    */
  def addPlayers(players: List[Player]) = players.foreach(p => addPlayer(p))

  /**
    * Defines if the game is over.
    * The game is over if one of the players' scores is above the maximum (100)
    * @return true if the game is over
    */
  def gameOver(): Boolean = {
    gameScores.find(s => s._2 > MAX_SCORE) match {
      case Some(_) => true
      case None    => false
    }
  }

  /**
    * This function updates the game scores by adding the last match scores for every player
    * @param matchScores the scores of the last match
    */
  def updateScores(matchScores: Map[Player, Int]) =
    gameScores = for {
      gs <- gameScores
      ms <- matchScores
      if gs._1 == ms._1
    } yield gs._1 -> (gs._2 + ms._2)

  /**
    * Create a new match
    */
  def newMatch() = {
    if(matches.isEmpty) { // pick a random player to start the first match
      val index = new Random(System.currentTimeMillis()).nextInt(players.length)
      firstPlayer = players.apply(index)
    } else { // all following matches are started by the players in "clockwise" order
      firstPlayer = nextPlayer(players, firstPlayer)
    }
    matches = new Match(firstPlayer) :: matches
  }

  def topOfOpenedDeck(): Card = matches.head.openedDeck.top()

  /* **************** Methods propagated to the Turn inner class ************************ */
  def curPlayer(): Player = matches.head.curRound.curTurn.player
  def pickCardFromOpenedDeck(): Card = matches.head.curRound.curTurn.pickCardFromOpenedDeck()
  def pickCardFromClosedDeck(): Card = matches.head.curRound.curTurn.pickCardFromClosedDeck()
  def dropCardToOpenedDeck(): Card = matches.head.curRound.curTurn.dropCardToOpenedDeck()
  def replaceCard(index: Int): Card = matches.head.curRound.curTurn.replacePlayersCard(index)
  def viewPlayersCard(target: Player, index: Int): Card =
    matches.head.curRound.curTurn.viewPlayersCard(target, index)
  def exchangeCards(target: Player, hCardIndex: Int, tCardIndex: Int): Card =
    matches.head.curRound.curTurn.exchangeCards(target, hCardIndex, tCardIndex)
  def declareLastRound() = matches.head.curRound.curTurn.declareLastRound()
  def lastRoundIsDeclared(): Boolean = matches.head.curRound.curTurn.lastRoundIsDeclared()
  def getHand(): Card = matches.head.curRound.curTurn.getHand()
  def cardIsPicked(): Boolean = matches.head.curRound.curTurn.cardPicked
  def cardIsDropped(): Boolean = matches.head.curRound.curTurn.cardDropped
  def cardPickedFromOpenedDeck(): Boolean = matches.head.curRound.curTurn.pickedFromOpenedDeck
  def turnFinished(): Boolean = matches.head.curRound.curTurn.turnFinished()
  def roundFinished(): Boolean = matches.head.curRound.roundFinished()



  /* **************** Methods controlling the game flow ************************ */
  def nextTurn() = matches.head.curRound.nextTurn()
  def nextRound() = matches.head.nextRound()

  class Match(first: Player) {

    /**
      * Initialize the match:
      * - resets the Deck52, so it contains all of 52 cards
      * - distributes 4 cards to every player of the game
      */
    Deck52.reset()
    Deck52.deck.shuffle()
    players.foreach(p => {
      p.dropCards()
      p.putCards(Deck52.deck.pickCards(4))
    })

    var lastRound = false
    // contains every Player's score
    var matchScores: Map[Player, Int] = players.map(p => (p, 0)).toMap
    var openedDeck = new Deck(List())
    var curRound = new Round(first)

    /**
      * @return true if the Match is over
      */
    def matchFinished(): Boolean = lastRoundIsDeclared() && lastRound && curRound.roundFinished()

    /**
      * Starts a new Round
      * The first player is randomly chosen
      */
    def nextRound() = {
      if (lastRoundIsDeclared())
        lastRound = true
      curRound = new Round(nextPlayer(players, curPlayer()))
    }

    /**
      * Calculates the scores players have
      * To use in the end of the match
      */
    def calculateScore() =
      players.foreach(p => // for every player update the scores map
        matchScores = matchScores + (p -> // by updating every key
          p.cards.map(card => card.points).sum) // calculating the score corresponding to
        // all cards that the player holds
      )

    class Round(first: Player) {
      private var curPlayer = first
      var curTurn = new Turn(first)

      /**
        * @return true if the Round is over
        */
      def roundFinished(): Boolean = nextPlayer(players, curPlayer) == this.first || lastRoundIsDeclared()

      def getCurPlayer(): Player = curPlayer

      def nextTurn() = {
        curPlayer = nextPlayer(players, curPlayer)
        curTurn = new Turn(curPlayer)
      }

      /**
        * - the player picks a card from an opened or a closed deck
        * - from opened => replace with one of his cards
        * - from closed => replace or drop
        * - the replaced or dropped card goes to the opened deck
        * @param p the player who plays the turn
        */
      class Turn(p: Player) {

        val player = p

        private var lastRoundDeclared = false

        private var hand: Card = _

        /**
          * picked && pickedFromOpenedDeck => pickedFromOpenedDeck
          * picked && !pickedFromOpenedDeck => pickedFromClosedDeck
          */
        var cardPicked: Boolean = false
        var cardDropped: Boolean = false
        var pickedFromOpenedDeck: Boolean = false

        def getHand(): Card = hand

        /**
          * The player picks a card from a deck
          * @param deck the deck to pick from
          * @return the card picked
          *         if the deck is empty, null is returned
          */
        private def pickCard(deck: Deck): Card = {
          if (deck.isEmpty) null
          else if (hand == null) {
            hand = deck.pickCard()
            cardPicked = true
            hand
          }
          else throw new Error("The player " + player.name + " holds a card already")
        }

        /**
          * The player drops the card to a deck
          * @param deck the deck to drop to
          * @return the card dropped
          */
        private def dropCard(deck: Deck): Card = {
          if (hand == null) throw new Error("Player " + player.name + " does not have a card in the hand")
          val droppedCard = hand
          deck.putCard(hand)
          hand = null
          cardDropped = true
          droppedCard
        }

        /**
          * The player picks a card from the opened deck
          * @return the card picked
          */
        def pickCardFromOpenedDeck(): Card = {
          val picked = pickCard(openedDeck)
          pickedFromOpenedDeck = true
          picked
        }

        /**
          * The player picks a card from the closed deck
          * @return the card picked
          */
        def pickCardFromClosedDeck(): Card = {
          val picked = pickCard(Deck52.deck)
          pickedFromOpenedDeck = false
          picked
        }

        /**
          * The player drops the card to the opened deck
          * @return the card dropped
          */
        def dropCardToOpenedDeck(): Card = dropCard(openedDeck)


        /**
          * Te player drops the card to the closed deck
          * TODO: do we really need it? Might happen if the player has made a mistake
          * @return
          */
        def dropCardToClosedDeck(): Card = dropCard(Deck52.deck)

        /**
          * Replace one of the Player's cards by the one he holds in his hand
          * and drop the replaced card to the opened deck
          * @param index index of the card of the Player's card set to replace
          * @return the card dropped to the opened deck
          */
        def replacePlayersCard(index: Int) = {
          hand = player.replaceCard(hand, index)
          dropCard(openedDeck)
        }

        /**
          * This feature is applied when the current player picks a Jack from the closed deck
          * and then drops it to the opened deck
          * @param target the Player who's card the current Player desires to see (target player)
          * @param index the index of the card from the target Player's card set
          * @return
          */
        def viewPlayersCard(target: Player, index: Int): Card = {
          target.seeCard(index)
        }

        /**
          * This feature is applied when the Queen special card is picked from the closed deck
          * and then dropped to the opened deck
          * The holder of the card must exchange one of his cards with one of the opponent player's cards
          * @param target the player with who the holder will exchange
          * @param hCardIndex the index of the holder's card to exchange
          * @param tCardIndex the index of the target player's card to exchange
          */
        def exchangeCards(target: Player, hCardIndex: Int, tCardIndex: Int) = {
          val card = target.replaceCard(player.seeCard(hCardIndex), tCardIndex)
          player.replaceCard(card, hCardIndex)
        }

        /**
          * @return true if the player who plays the turn declares the last round
          */
        def lastRoundIsDeclared(): Boolean = lastRoundDeclared

        /**
          * The player who plays the turn declares the last round
          * The player can only declare the last round after he finishes his turn
          */
        def declareLastRound() = if (turnFinished()) lastRoundDeclared = true

        /**
          * @return true if the current player has finished his turn
          */
        def turnFinished(): Boolean = cardPicked && cardDropped
      }

    }
  }
}
