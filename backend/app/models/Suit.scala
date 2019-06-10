package models

/**
  * This Enumeration contains all the suits of the classic 52-card deck
  */
object Suit extends Enumeration {
  type Suit = Value
  val spades   = Value("spades")   // "\u2660"
  val hearts   = Value("hearts")   // "\u2665"
  val diamonds = Value("diamonds") // "\u2666"
  val clubs    = Value("clubs")    // "\u2663"
  val closed   = Value("closed")
  val empty    = Value("empty")
}

