import React, { Fragment, Component } from 'react'
import PropTypes from 'prop-types'
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

const INITIAL_STATE = {
}

class App extends Component {

  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
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
            <PrivateRoute path={PATHS.LOBBY} component={LobbyPage} />
            <PrivateRoute path={PATHS.BOARD} component={BoardPage} />
          </Switch>
        </div>
      </Fragment>
    )
  }
}

App.propTypes = {
  location: PropTypes.shape({
    pathname: PropTypes.string.isRequired,
  }).isRequired,
}

export default withRouter(App)
