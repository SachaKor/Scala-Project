import React, { Component } from 'react'

import Deck from '../deck/Deck'
import CommonDeck from '../deck/CommonDeck'

import './BoardPage.scss'

const INITIAL_STATE = {

}

class BoardPage extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  render() {
    return (
      <div className="card-decks-container">
        <div className="turn">
          Turn: Player 1
        </div>
        <div className="container-row top-row">
          <Deck rotate={1} />
        </div>
        <div className="container-row middle-row">
          <Deck rotate={2} />
          <CommonDeck />
          <Deck rotate={3} />
        </div>
        <div className="container-row bottom-row">
          <Deck />
        </div>
      </div>
    )
  }
}

export default BoardPage
