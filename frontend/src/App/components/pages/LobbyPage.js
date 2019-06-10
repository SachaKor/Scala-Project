import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'
import { withRouter } from 'react-router'

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
    this.props.setSocket()
  }

  componentDidUpdate() {
    if(this.props.socket) {
      this.props.socket.onmessage = this.handleMessages
    }
  }

  handleMessages = (e) => {
    console.log('Received message from server.')
    const message = JSON.parse(e.data)
    console.log(message)
    switch(message.eventType) {
      case 'nbPlayers':
        this.setState({
          nbPlayers: message.eventContent.nbPlayers,
          joined: true
        })
        break;
        case 'startGame':
          this.props.history.push('/board')
          break;
      default:
        console.log("Unknown")
    }
  }

  handleJoin = () => {
    console.log('join')
    this.props.socket.send(JSON.stringify({eventType: "join"}))
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

export default withRouter(LobbyPageWithSocket)
