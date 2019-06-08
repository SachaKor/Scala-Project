package models

import play.api.libs.json.{JsString, JsValue, Json}

class Player(n: String, c: List[Card]) {
  var cards: List[Card] = c
  val name: String = n
  var hand: Card = _ // the card that the player holds in his hand (picked from a deck)

  /**
    * Picks a card from the Player's cards set
    * @param index index of the card to pick
    * @return the card picked
    */
  def seeCard(index: Int): Card = {
    // TODO: can be replaced with cards.apply(index)
    if (cards.isEmpty) throw new Error("Player " + name + " has no cards")
    if(index >= cards.size) throw new Error("Player " + name + " only has " + cards.size + " cards")
    def loop(index: Int, cards: List[Card]): Card = index match {
      case 0 => cards.head
      case _ => loop(index-1, cards.tail)
    }
    loop(index, cards)
  }

  /**
    * Replaces one of the Player's card by another one
    * @param card the card to put to the Player's card set
    * @param index the index of the card to replace
    * @return the replaced card
    */
  def replaceCard(card: Card, index: Int): Card = {
    if(index < 0 || index >= cards.size) throw new Error("Error in card index: " +
      "["+ index.toString+ "]; player " + name + " has " + cards.size + " cards")
    val replaced = cards.apply(index)
    cards = cards.take(index) ++ List(card) ++ cards.drop(index+1)
    replaced
  }

  /**s
    * Puts the card to the end of the Player's card set
    * @param card
    */
  def putCard(card: Card) = cards = cards ++ List(card)

  /**
    * Puts multiple Cards to the Player's card set
    * @param cards
    */
  def putCards(cards: List[Card]) = this.cards = this.cards ++ cards

  /**
    * The Player drops all of his cards
    */
  def dropCards() = cards = List()

  def toJson(indices: List[Int]): JsValue = {
    if(indices.length > this.cards.length)
      throw new Error("Too many cards to show")
    val cardsToShow: List[Card] = indices.map(i => seeCard(i))
    val cards: List[Card] = this.cards.map(c =>
      if (cardsToShow.contains(c)) c
      else new Card(Rank.closed, Suit.closed, -1))
    Json.obj(
      "name"-> this.name,
      "cards" -> new CardList(cards).toJson()
    )
  }

  override def toString: String = name + ": " + cards.map(c => c.toString + " ")
}
