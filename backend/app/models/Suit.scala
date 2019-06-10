package models

/**
  * This Enumeration contains all the suits of the classic 52-card deck
  */
object Suit extends Enumeration {
  type Suit = Value
  val spades   = Value("S")   // "\u2660"
  val hearts   = Value("H")   // "\u2665"
  val diamonds = Value("D") // "\u2666"
  val clubs    = Value("C")    // "\u2663"
  val closed   = Value("closed")
  val empty    = Value("empty")
}

