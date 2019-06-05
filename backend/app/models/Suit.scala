package models

/**
  * This Enumeration contains all the suits of the classic 52-card deck
  */
object Suit extends Enumeration {
  type Suit = Value
  val spades   = Value("\u2660")
  val hearts   = Value("\u2665")
  val diamonds = Value("\u2666")
  val clubs    = Value("\u2663")
}

