import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'
import Deck from '../deck/Deck'
import CommonDeck from '../deck/CommonDeck'

import './BoardPage.scss'

const INITIAL_STATE = {
  socker: null
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

export default BoardPage
