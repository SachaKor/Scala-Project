import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'

import { PATHS } from '../routes'
import './Header.scss'

const INITIAL_STATE = {

}

class Header extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  render() {
    const { loggedIn } = this.props

    return (
      <div className="header-root">
        <div className="header-container">
          <div className="header-logo">
            <Link to={PATHS.HOME}>
              YOUGLOUF
            </Link>
          </div>
          <div className="header-button">
            <Link to={PATHS.LOGIN}>
              { loggedIn ? "LOG OUT" : "LOG IN" }
            </Link>
          </div>
        </div>
      </div>
    )
  }
}

function mapStateToProps(state) {
  const { loggedIn } = state.authentication

  return {
    loggedIn
  }
}

const connectedLoginForm = connect(mapStateToProps)(Header)

export default connectedLoginForm
