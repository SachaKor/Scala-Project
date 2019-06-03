package models

object Main extends App {

//  testCard()
//  testDeck()
//  testPlayer()
  testGame()

  def testGame() = {
    println("...simulating a game...")
    // Create 4 players
    val players: List[Player] = List(
      new Player("Arnold", List()),
      new Player("PS", List()),
      new Player("Sam", List()),
      new Player("Sasha", List())
    )
    Game.addPlayers(players)
    println("Players do not have cards yet")
    Game.players.foreach(p => println(p.toString))
    Game.newMatch()
    println("First match started, cards distributed")
    Game.players.foreach(p => println(p.toString))
    val curPlayer =  Game.curPlayer()
    println("Current player: " + curPlayer)
    // current player picks a card from the closed deck
    val picked = Game.pickCardFromClosedDeck()
    println(curPlayer.name + " picked " + picked)
    val replaced = Game.replaceCard(0)
    println(curPlayer.name + " replaced " + replaced + " card")
    println(curPlayer.toString)

  }

  def testPlayer() = {
    println("\n...testing models.Player class...")
    Deck52.deck.shuffle()
    val player = new Player("sasha", List(new Card(
      Rank.king,
      Suit.hearts,
      CardPoints.points(Rank.king, Suit.hearts)
    )))
    println("Sasha's initial card set:")
    println(player.cards.map(_.toString))
    player.putCard(Deck52.deck.pickCard())
    println("After picking a card from the deck")
    println(player.cards.map(_.toString))
    println("After picking 4 cards from the deck")
    player.putCards(Deck52.deck.pickCards(4))
    println(player.cards.map(_.toString))
    val replaced = player.replaceCard(Deck52.deck.pickCard(), 1)
    println("After replacing the 1st card")
    println(player.cards.map(_.toString))
    println("The replaced card: " + replaced.toString)
    // now check if the error is thrown
//    player.replaceCard(Deck52.deck.pickCard(), 15)
    // it is
  }

  def testDeck() = {
    println("\n...testing models.Deck class...")
    println("Deck52: ")
    println(Deck52.deck.toString)
    println("Deck52 size: " + Deck52.deck.size())
    println("Points of the cards in Deck52:")
    Deck52.deck.cards.foreach(c => print(c.points + " "))
    println("\nDeck52 after shuffle:")
    Deck52.deck.shuffle()
    println(Deck52.deck.toString)
    println("Pick a card")
    val card = Deck52.deck.pickCard()
    println(Deck52.deck.toString + "\nPicked card: " + card.toString)
    println("Put a picked card back to the deck")
    Deck52.deck.putCard(card)
    println(Deck52.deck)
    println("Pick 4 cards from the deck")
    val cards = Deck52.deck.pickCards(4)
    println(Deck52.deck.toString)
    println("Picked cards: " + cards.map(_.toString))
  }

  def testCard() = {
    println("\n...testing models.Card class...")
    val cards = List(
      new Card(Rank.queen, Suit.spades, 12),
      new Card(Rank.jack, Suit.diamonds, 11),
      new Card(Rank.eight, Suit.hearts, 8),
      new Card(Rank.five, Suit.clubs, 5))

    cards.foreach(println)

    val spadesRedQueen = new Card(Rank.queen, Suit.spades, 12)
    require(cards.head.equals(spadesRedQueen))
  }
}
