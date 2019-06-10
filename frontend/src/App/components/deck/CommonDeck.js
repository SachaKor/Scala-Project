import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'

import card from '../../assets/cards/back.png'
import card2 from '../../assets/cards/ace_of_spades.png'

import './Deck.scss'

const INITIAL_STATE = {

}

class CommonDeck extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  handleClosedClick = () => {
    this.props.socket.send(JSON.stringify({eventType: "pickCardFromOpenedDeck"}))
  }

  handleOpenedClick = () => {
    this.props.socket.send(JSON.stringify({eventType: "pickCardFromClosedDeck"}))
  }

  render() {
    const { openedDeck } = this.props
    console.log(openedDeck)

    return (
      <div className="deck common">
        <div className="img-container common">
          <img src={require('./PNG/closedclosed.png')} alt="" onClick={this.handleClosedClick} />
        </div>
        { this.props.openedDeck && this.props.openedDeck !== "empty" &&
          (<div className="img-container common">
            <img src={require(`./PNG/${openedDeck.rank}${openedDeck.suit}.png`)} alt="" onClick={this.handleOpenedClick} />
          </div>) }
      </div>
    )
  }
}

const DeckWithSocket = props => (
  <SocketContext.Consumer>
    {socket => <CommonDeck {...props} socket={socket} />}
  </SocketContext.Consumer>
)

export default DeckWithSocket
