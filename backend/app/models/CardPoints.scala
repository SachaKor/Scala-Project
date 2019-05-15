package models

import models.Rank.Rank
import models.Suit.Suit

/**
  * This object defines the points attributed to every Card in a classic 52-card deck
  */
object CardPoints {
  def points(rank: Rank, suit: Suit): Int = (rank, suit) match {
      case (Rank.king, Suit.hearts)   => 0
      case (Rank.king, Suit.diamonds) => 0
      case (Rank.king, _)             => 13
      case (Rank.queen, _)            => 12
      case (Rank.jack, _)             => 11
      case (Rank.ace, _)              => 1
      case (numRank, _)               => numRank.toString.toInt // rank is a numeric value -> points are equal to the rank
  }
}
