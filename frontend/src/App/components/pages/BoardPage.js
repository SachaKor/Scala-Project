import React, { Component } from 'react'
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
  socket: null,
  curPlayer: INIT_PLAYER,
  me: INIT_PLAYER,
  others: [
    INIT_PLAYER,
    INIT_PLAYER,
    INIT_PLAYER,
    INIT_PLAYER
  ],
  openedDeck: {rank: "empty", suit: "empty"},
  hand: {rank: "empty", suit: "empty"},
  picked: false
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
      socket: socket
    })

    socket.onopen = () => {
      console.log('connected')
      socket.send(JSON.stringify({eventType: "getCards", eventContent: {}}))
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

  setCurrentState = (content) => {
    const { curPlayer, me, others, openedDeck, hand } = content
    this.setState({
      curPlayer,
      me,
      others,
      openedDeck,
      hand
    })
  }

  handleMessages = (e) => {
    console.log('Received message from server.')
    const message = JSON.parse(e.data)
    console.log(message)
    switch(message.eventType) {
      case 'getCards':
        console.log(message.eventContent)
        this.setCurrentState(message.eventContent)
        break
      case 'cardClick':
        console.log(message.eventContent)
        break
      case 'notifyChange':
        console.log(message.eventContent)
        this.state.socket.send(JSON.stringify({eventType: "getCards", eventContent: {}}))
        break
      default:
        console.log("Unknown")
    }
  }

  render() {
    const { socket, openedDeck, hand, curPlayer, me, others } = this.state

    return (
      <div className="card-decks-container">
        <div className="turn">
          Turn: {curPlayer.name}
        </div>
        <div className="hand">
          <img src={require(`../deck/PNG/${hand.rank}${hand.suit}.png`)} alt="" onClick={this.handleOpenedClick} />
        </div>
        <div className="container-row top-row">
          <div className="name">{others[0].name}</div>
          <Deck rotate={1} cards={INIT_PLAYER.cards} socket={socket} />
        </div>
        <div className="container-row middle-row">
          <Deck rotate={2} cards={INIT_PLAYER.cards} socket={socket} />
          <CommonDeck
            openedDeck={openedDeck}
            socket={socket}
          />
          <Deck rotate={3} cards={INIT_PLAYER.cards} socket={socket} />
        </div>
        <div className="container-row bottom-row">
          <Deck rotate={-1} cards={me.cards} socket={socket} name={me.name} />
        </div>
      </div>
    )
  }
}

export default BoardPage
