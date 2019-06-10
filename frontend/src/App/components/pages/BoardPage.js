import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'
import Deck from '../deck/Deck'
import CommonDeck from '../deck/CommonDeck'

import './BoardPage.scss'

const INIT_PLAYER = {
  name: "",
  cards: [
    {rank: "closed", suit: "closed"},
    {rank: "closed", suit: "closed"},
    {rank: "closed", suit: "closed"},
    {rank: "closed", suit: "closed"},
  ]
}

const INITIAL_STATE = {
  socker: null,
  curPlayer: INIT_PLAYER,
  me: INIT_PLAYER,
  others: [
    INIT_PLAYER,
    INIT_PLAYER,
    INIT_PLAYER,
    INIT_PLAYER
  ],
  openedDeck: null,
}

class BoardPage extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  componentDidMount() {
    this.connectSocket()
  }

  connectSocket = () => {
    const user = JSON.parse(localStorage.getItem('user'))
    const URL = 'ws://localhost:9000/ws?token=' + user.token

    const socket = new WebSocket(URL)
    socket.onmessage = this.handleMessages

    this.setState({
      socket
    })

    socket.onopen = () => {
      console.log('connected')
      socket.send(JSON.stringify({eventType: "getCards"}))
    }

    socket.onclose = (e) => {
      console.log('Socket is closed. Reconnect will be attempted in 1 second.', e.reason);
      setTimeout(() => {
        this.connectSocket()
      }, 1000)
    }

    socket.onerror = (err) => {
      console.error('Socket encountered error: ', err.message, 'Closing socket');
      socket.close()
    }
  }

  handleMessages = (e) => {
    console.log('Received message from server.')
    const message = JSON.parse(e.data)
    console.log(message)
    switch(message.eventType) {
      case 'getCards':
        console.log(message.eventContent)
        this.setState({
          curPlayer: message.eventContent.curPlayer,
          me: message.eventContent.me,
          others: message.eventContent.others,
          openedDeck: message.eventContent.openedDeck,
          closedDeck: message.eventContent.closedDeck,
        })
        break;
      default:
        console.log("Unknown")
    }
  }

  render() {
    const { socket } = this.state
    return (
      <div className="card-decks-container">
        <div className="turn">
          Turn: {this.state.curPlayer.name}
        </div>
        <div className="container-row top-row">
          <Deck rotate={1} cards={INIT_PLAYER.cards} socket={socket} />
        </div>
        <div className="container-row middle-row">
          <Deck rotate={2} cards={INIT_PLAYER.cards} socket={socket} />
          <CommonDeck
            openedDeck={this.state.openedDeck}
          />
          <Deck rotate={3} cards={INIT_PLAYER.cards} socket={socket} />
        </div>
        <div className="container-row bottom-row">
          <Deck rotate={-1} cards={this.state.me.cards} socket={socket} />
        </div>
      </div>
    )
  }
}

export default BoardPage
