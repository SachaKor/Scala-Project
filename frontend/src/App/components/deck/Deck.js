import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'

import card from '../../assets/cards/back.png'

import './Deck.scss'

const INITIAL_STATE = {

}

class Deck extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  render() {
    const { rotate } = this.props
    let className = ""
    className += rotate === 1 ? " top-deck" : ""
    className += rotate === 2 ? " left-deck" : ""
    className += rotate === 3 ? " right-deck" : ""

    return (
      <div className={"deck" + className}>
        <div className="cards">
          <div className="cards-row">
            <img src={card} alt="" />
            <img src={card} alt="" />
            <img src={card} alt="" />
          </div>
          <div className="cards-row">
            <img src={card} alt="" />
            <img src={card} alt="" />
          </div>
        </div>
      </div>
    )
  }
}

const DeckWithSocket = props => (
  <SocketContext.Consumer>
    {socket => <Deck {...props} socket={socket} />}
  </SocketContext.Consumer>
)

export default DeckWithSocket
