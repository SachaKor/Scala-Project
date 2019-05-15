package models

object Main extends App {

  testCard()
  testDeck()
  testPlayer()

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
    require(cards.head == spadesRedQueen)
  }
}
