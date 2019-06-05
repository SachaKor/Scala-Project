import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'

import Deck from '../deck/Deck'
import card from '../../assets/cards/ace_of_spades.png'

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
          <Deck />
        </div>
        <div className="container-row middle-row">
          <Deck rotate={1} />
          <Deck />
          <Deck rotate={-1} />
        </div>
        <div className="container-row bottom-row">
          <Deck />
        </div>
      </div>
    )
  }
}

const BoardPageWithSocket = props => (
  <SocketContext.Consumer>
    {socket => <BoardPage {...props} socket={socket} />}
  </SocketContext.Consumer>
)

export default BoardPageWithSocket
