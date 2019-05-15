package models

import models.Rank.Rank
import models.Suit.Suit

class Card(r: Rank, s: Suit, p: Int) {
  val rank = r
  val suit = s
  val points = p

  override def toString: String = {

    "[" + rank.toString + s.toString + "]"
  }

  def ==(other: Card): Boolean = (rank == other.rank) && (suit == other.suit)
}
