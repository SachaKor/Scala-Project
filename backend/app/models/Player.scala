package models

class Player(name: String, c: List[Card]) {
  var cards = c

  /**
    * Picks a card from the Player's cards set
    * @param index index of the card to pick
    * @return the card picked
    */
  def pickCard(index: Int) = {
    if (cards.isEmpty) throw new Error("Player " + name + " has no cards")
    def loop(index: Int, cards: List[Card]): Card = index match {
      case 0 => cards.head
      case _ => loop(index-1, cards.tail)
    }
    loop(index, cards)
  }

  /**
    * Puts the card to the end of the Player's card set
    * @param card
    */
  def putCard(card: Card) = cards = cards ++ List(card)

  /**
    * Puts multiple Cards to the Player's card set
    * @param cards
    */
  def putCards(cards: List[Card]) = this.cards = this.cards ++ cards
}
