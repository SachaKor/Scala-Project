package models

/**
  * This object represents a classic 52-card deck
  */
object Deck52 {
  var deck: Deck = _

  def reset() = {
    deck = new Deck(
      for {
        rank <- Rank.values.toList
        suit <- Suit.values.toList
      } yield new Card(rank, suit, CardPoints.points(rank, suit))
    )
  }
  reset()
}
