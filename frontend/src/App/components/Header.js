import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import api from '../../helpers/api'

import { ReactComponent as UserIcon } from '../assets/svg/user.svg'

import { PATHS } from '../routes'
import './Header.scss'

const INITIAL_STATE = {

}

class Header extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  handleClick = () => {
    console.log('click')
    api.get('/secure').then(
      (response) => {
        console.log(response)
      },
      (error) => {
        console.log(error)
      }
    )
  }

  render() {
    return (
      <div className="header-root">
        <div className="header-container">
          <div className="header-logo">
            <Link to={PATHS.HOME}>
              YOUGLOUF
            </Link>
          </div>
          <div className="header-button">
            <button onClick={this.handleClick}>
              TEST
            </button>
            <Link to={PATHS.LOGIN}>
              LOGIN
            </Link>
          </div>
        </div>
      </div>
    )
  }
}

export default Header
