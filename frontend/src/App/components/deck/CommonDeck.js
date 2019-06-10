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

  handleClick = (deck) => {
    this.props.socket.send(JSON.stringify({
      eventType: "cardClick",
      eventContent: {
        index: "nope",
        name: "nope",
        deck: deck
      }
    }))
  }

  handleNothing = () => {
    console.log('Denied')
  }

  render() {
    const { openedDeck } = this.props
    // let handleOpened = this.handleNothing
    // let handleClosed = this.handleNothing
    // if(turn) {
    //   if(picked) {
    //     handleOpened = this.handleDropOpenedClick
    //   } else {
    //     handleClosed = this.handlePickClosedClick
    //     handleOpened = this.handlePickOpenedClick
    //   }
    // }

    return (
      <div className="deck common">
        <div className="img-container common">
          <img src={require('./PNG/closedclosed.png')} alt="" onClick={() => this.handleClick("closed")} />
        </div>
        <div className="img-container common">
          <img src={require(`./PNG/${openedDeck.rank}${openedDeck.suit}.png`)} alt="" onClick={() => this.handleClick("opened")} />
        </div>
      </div>
    )
  }
}

export default CommonDeck
