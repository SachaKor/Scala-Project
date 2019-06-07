import React, { Fragment } from 'react'
import { Switch, Route, withRouter } from 'react-router-dom'
import SocketContext from './utils/SocketContext'
import PrivateRoute from '../components'
import * as io from 'socket.io-client'

import Header from './components/Header.js'
import LoginPage from './components/pages/LoginPage.js'
import SignupPage from './components/pages/SignupPage.js'
import LobbyPage from './components/pages/LobbyPage.js'
import BoardPage from './components/pages/BoardPage.js'

import { PATHS } from './routes'
import './App.scss'

// const socket = io('http://localhost:9000')
const socket = null

function App() {
  return (
    <Fragment>
      <Header />
      <div className="main-container">
        <Switch>
          <Route path={PATHS.HOME} exact component={BoardPage} />
          <Route path={PATHS.LOGIN} exact component={LoginPage} />
          <Route path={PATHS.SIGNUP} exact component={SignupPage} />
          <SocketContext.Provider value={socket}>
            <PrivateRoute path={PATHS.LOBBY} component={LobbyPage} />
            <PrivateRoute path={PATHS.BOARD} component={BoardPage} />
          </SocketContext.Provider>
        </Switch>
      </div>
    </Fragment>
  )
}

export default withRouter(App)
