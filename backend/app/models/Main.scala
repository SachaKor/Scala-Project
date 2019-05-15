package models

object Main extends App {

  testCard()
  testDeck()

  def testDeck() = {
    println("...testing models.Deck class...")
    println(Deck52.deck.toString)
    println("Deck52 size: " + Deck52.deck.size())
  }

  def testCard() = {
    println("...testing models.Card class...")
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
