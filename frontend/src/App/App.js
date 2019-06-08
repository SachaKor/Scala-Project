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

class App extends Component {

  socket = new WebSocket(URL)

  componentDidMount() {
    this.socket.onopen = () => {
      // on connecting, do nothing but log it to the console
      console.log('connected')
    }

    this.socket.onmessage = evt => {
      // on receiving a message, add it to the list of messages
      const message = JSON.parse(evt.data)
      this.addMessage(message)
    }

    this.socket.onclose = () => {
      console.log('disconnected')
      // automatically try to reconnect on connection loss
      this.setState({
        ws: new WebSocket(URL),
      })
    }
  }

  render() {
    return (
      <Fragment>
        <Header />
        <div className="main-container">
          <Switch>
            <Route path={PATHS.HOME} exact component={BoardPage} />
            <Route path={PATHS.LOGIN} exact component={LoginPage} />
            <Route path={PATHS.SIGNUP} exact component={SignupPage} />
            <PrivateRoute path={PATHS.LOBBY} component={
                <SocketContext.Provider value={"this.socket"}>
                  {LobbyPage}
                </SocketContext.Provider>
              } />
            <PrivateRoute path={PATHS.BOARD} component={
                <SocketContext.Provider value={"this.socket"}>
                  {BoardPage}
                </SocketContext.Provider>
              } />
          </Switch>
        </div>
      </Fragment>
    )
  }
}

export default withRouter(App)
