import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'

import card from '../../assets/cards/ace_of_spades.png'

import './Deck.scss'

const INITIAL_STATE = {

}

class CommonDeck extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  handleClick = () => {
    this.props.socket.send({type: "join"})
  }

  render() {
    const { rotate } = this.props
    let className = ""
    className += rotate === 1 ? " left-deck" : ""
    className += rotate === -1 ? " right-deck" : ""

    return (
      <div className={"deck common" + className}>
        <div className="cards">
          <div className="cards-row common">
            <img src={card} alt="" onClick={this.handleClick} />
            <img src={card} alt="" onClick={this.handleClick} />
          </div>
        </div>
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
