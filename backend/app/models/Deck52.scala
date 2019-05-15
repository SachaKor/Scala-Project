package models

/**
  * This object represents a classic 52-card deck
  */
object Deck52 {

  private val cards: List[Card] = for {
    rank <- Rank.values.toList
    suit <- Suit.values.toList
  } yield new Card(rank, suit, CardPoints.points(rank, suit))

  val deck = new Deck(cards)
}
