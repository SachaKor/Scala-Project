package models

import models.Rank.Rank
import models.Suit.Suit

/**
  * This class represents a card
  * @param r Rank of the card
  * @param s Suit of the card
  * @param p Points attributed to the card
  */
class Card(r: Rank, s: Suit, p: Int) {
  val rank = r
  val suit = s
  val points = p

  override def toString: String = "[" + rank.toString + s.toString + "]"

  /**
    * Verifies if 2 Cards are equal
    * @param other the Card to compare to
    * @return true if two Cards are equal
    */
  def equals(other: Card): Boolean = {
    (rank == other.rank) && (suit == other.suit)
  }
}
