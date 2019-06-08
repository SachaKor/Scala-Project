package models

import play.api.libs.json.{JsValue, Json}

class CardList(cards: List[Card]) {
  val cardIndex = cards.map(c => (cards.indexOf(c), c))
  def toJson(): JsValue =
    Json.toJson(cardIndex.map(ci => Json.obj(
      "index" -> ci._1,       // key
              "card" -> ci._2.toJson  //value
    )))
}
