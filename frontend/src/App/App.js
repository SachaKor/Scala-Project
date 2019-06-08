import React, { Fragment, Component } from 'react'
import { Switch, Route, withRouter } from 'react-router-dom'
import SocketContext from './utils/SocketContext'
import PrivateRoute from '../components'
import Header from './components/Header.js'
import LoginPage from './components/pages/LoginPage.js'
import SignupPage from './components/pages/SignupPage.js'
import LobbyPage from './components/pages/LobbyPage.js'
import BoardPage from './components/pages/BoardPage.js'
import { PATHS } from './routes'
import './App.scss'

const URL = 'ws://localhost:9000/ws'

const INITIAL_STATE = {
  socket: null
}

class App extends Component {

  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  componentDidMount() {
    this.connectSocket()
  }

  connectSocket = () => {
    const user = JSON.parse(localStorage.getItem('user'))
    const socket = new WebSocket(URL, user.token)

    this.setState({
      socket
    })

    socket.onopen = () => {
      console.log('connected')
    }

    socket.onmessage = (e) => {
      console.log('Message:', e.data);
    };

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
      <Fragment>
        <Header />
        <div className="main-container">
          <Switch>
            <Route path={PATHS.HOME} exact component={LoginPage} />
            <Route path={PATHS.LOGIN} exact component={LoginPage} />
            <Route path={PATHS.SIGNUP} exact component={SignupPage} />
            <SocketContext.Provider value={this.state.socket}>
              <PrivateRoute path={PATHS.LOBBY} component={LobbyPage} />
              <PrivateRoute path={PATHS.BOARD} component={BoardPage} />
            </SocketContext.Provider>
          </Switch>
        </div>
      </Fragment>
    )
  }
}

export default withRouter(App)
