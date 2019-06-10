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

  connectSocket = () => {
    const user = JSON.parse(localStorage.getItem('user'))
    const { setSocket } = this.props

    const URL = 'ws://localhost:9000/ws?token=' + user.token

    const socket = new WebSocket(URL)

    this.setState({
      socket
    }, () => setSocket(this.state.socket))

    socket.onopen = () => {
      console.log('connected')
    }

    socket.onmessage = this.handleMessages

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

  render() {
    return (
      <div className="card-decks-container">
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
