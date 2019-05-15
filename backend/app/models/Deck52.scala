package models

object Deck52 {

  val cards: List[Card] = for {
    rank <- Rank.values.toList
    suit <- Suit.values.toList
  } yield new Card(rank, suit, -1)

  val deck = new Deck(cards)
}
