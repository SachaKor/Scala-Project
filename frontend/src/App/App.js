import React from 'react'
import { BrowserRouter as Router, Route } from 'react-router-dom'
import SocketContext from './utils/SocketContext'
import PrivateRoute from '../components'
import * as io from 'socket.io-client'

import Header from './components/Header.js'
import LoginPage from './components/pages/LoginPage.js'
import SigninPage from './components/pages/SigninPage.js'
import LobbyPage from './components/pages/LobbyPage.js'
import BoardPage from './components/pages/BoardPage.js'

import { PATHS } from './routes'
import './App.scss'

const socket = io('http://localhost:9000')

function App() {
  return (
    <Router>
      <Header />
      <div className="main-container">
        <Route path={PATHS.HOME} exact component={BoardPage} />
        <Route path={PATHS.LOGIN} exact component={LoginPage} />
        <Route path={PATHS.SIGNIN} exact component={SigninPage} />
        <SocketContext.Provider value={socket}>
          <PrivateRoute path={PATHS.LOBBY} component={LobbyPage} />
          <PrivateRoute path={PATHS.BOARD} component={BoardPage} />
        </SocketContext.Provider>
      </div>
    </Router>
  )
}

export default App
