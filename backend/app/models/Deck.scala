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

  /**
    * Shuffles the cards in the deck
    */
  def shuffle() = cards = scala.util.Random.shuffle(cards)

  /**
    * Picks cards from the top of the deck
    * @param n number of Cards to pick
    * @return the List containing n cards from the top of the deck
    */
  def pickCards(n: Int): List[Card] = {
    if (size() < n) throw new Error("Trying to pick " + n.toString + " cards from the deck containing " + size() + " cards")
    for {
      i <- (0 to n).toList
    } yield pickCard()
  }

  override def toString: String = cards.foldLeft("")((acc, card) => acc + card.toString + " ")

}
