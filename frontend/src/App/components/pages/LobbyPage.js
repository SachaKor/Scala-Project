import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'

import './LobbyPage.scss'

const INITIAL_STATE = {
  socket: null,
  nbPlayers: 0,
  joined: false
}

class LobbyPage extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  componentDidMount() {
    this.connectSocket()
  }

  handleMessages = (e) => {
    console.log('Received message from server.')
    const message = JSON.parse(e.data)
    switch(message.eventType) {
      case 'joined':
        this.setState({
          nbPlayers: message.eventContent.nbPlayers,
          joined: true
        })
        break;
      default:
        console.log("Not known")
    }
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

  handleJoin = () => {
    this.state.socket.send(JSON.stringify({eventType: "join"}))
  }

  render() {
    return (
      <div className="lobby-page-root">
        {this.state.joined ?
          <div className="info-box">{this.state.nbPlayers}/4 Players</div> :
          <div className="info-box">Click to join the game !</div>
        }
        <button
          type="submit"
          className="generic-btn-white"
          onClick={this.handleJoin}
        >
          JOIN GAME
        </button>
      </div>
    )
  }
}

const LobbyPageWithSocket = props => (
  <SocketContext.Consumer>
    {socket => <LobbyPage {...props} socket={socket} />}
  </SocketContext.Consumer>
)

export default LobbyPageWithSocket
