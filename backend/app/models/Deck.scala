package models

class Deck(c: List[Card]) {
  var cards = c

  /**
    * Pick a card from the top of the deck
    * @return the card from the top of the deck
    */
  def pickCard(): Card = {
    if (cards.isEmpty) throw new Error("Empty deck!")
    val top = cards.head
    cards = cards.tail
    top
  }

  /**
    * Put a card on top of the deck
    * @param card
    */
  def putCard(card: Card) = cards = card :: cards

  /**
    * @return the size of the deck
    */
  def size(): Int = cards.size

  override def toString: String = cards.foldLeft("")((acc, card) => acc + card.toString + " ")
}
