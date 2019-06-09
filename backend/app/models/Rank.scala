package models

/**
  * This Enumeration contains all ranks of the cards
  */
object Rank extends Enumeration {
  type Rank = Value
  val two   = Value("2")
  val three = Value("3")
  val four  = Value("4")
  val five  = Value("5")
  val six   = Value("6")
  val seven = Value("7")
  val eight = Value("8")
  val nine  = Value("9")
  val ten   = Value("10")
  val jack  = Value("J")
  val queen = Value("Q")
  val king  = Value("K")
  val ace   = Value("A")
  val closed = Value("closed")
}
