import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'

import './Deck.scss'

const INITIAL_STATE = {

}

class CommonDeck extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  handleClosedClick = () => {
    this.props.socket.send(JSON.stringify({eventType: "pickCardFromClosedDeck"}))
  }

  handleOpenedClick = () => {
    this.props.socket.send(JSON.stringify({eventType: "pickCardFromOpenedDeck"}))
  }

  render() {
    const { openedDeck } = this.props

    return (
      <div className="deck common">
        <div className="img-container common">
          <img src={require('./PNG/closedclosed.png')} alt="" onClick={this.handleClosedClick} />
        </div>
        <div className="img-container common">
          <img src={require(`./PNG/${openedDeck.rank}${openedDeck.suit}.png`)} alt="" onClick={this.handleOpenedClick} />
        </div>
      </div>
    )
  }
}

export default CommonDeck
