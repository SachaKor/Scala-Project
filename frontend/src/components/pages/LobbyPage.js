import React, { Component } from 'react'
import SocketContext from './utils/SocketContext'

const INITIAL_STATE = {

}

class LobbyPage extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  render() {
    return (
      <SocketContext.Consumer>
        <h1>Hello</h1>
      </SocketContext.Consumer>
    )
  }
}

const LobbyPageWithSocket = props => (
  <SocketContext.Consumer>
    {socket => <LobbyPage {...props} socket={socket} />}
  </SocketContext.Consumer>
)

export default LobbyPageWithSocket
