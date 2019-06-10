import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'
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

  componentDidMount() {
    if(this.props.socket) {
      console.log('socket')
      this.props.socket.onmessage = this.handleMessages
      this.props.socket.send(JSON.stringify({eventType: "getCards"}))
    }
  }

  componentDidUpdate() {
    if(this.props.socket) {
      console.log('socket')
      this.props.socket.onmessage = this.handleMessages
      this.props.socket.send(JSON.stringify({eventType: "getCards"}))
    }
  }

  handleMessages = (e) => {
    console.log('Received message from server.')
    const message = JSON.parse(e.data)
    console.log(message)
    switch(message.eventType) {
      case 'getCards':
        console.log(message.eventContent)
        break;
      default:
        console.log("Unknown")
    }
  }

  render() {
    return (
      <div className="card-decks-container">
        <div></div>
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

const BoardPageWithSocket = props => (
  <SocketContext.Consumer>
    {socket => <BoardPage {...props} socket={socket} />}
  </SocketContext.Consumer>
)

export default BoardPageWithSocket
