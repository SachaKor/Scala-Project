import React, { Component } from 'react'

import card from '../../assets/cards/ace_of_spades.png'

import './Deck.scss'

const INITIAL_STATE = {

}

class Deck extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  render() {
    const { rotate } = this.props
    let className = ""
    className += rotate === 1 ? " left-deck" : ""
    className += rotate === -1 ? " right-deck" : ""

    return (
      <div className={"deck" + className}>
        <div className="cards">
          <div className="cards-row">
            <img src={card} alt="" />
            <img src={card} alt="" />
            <img src={card} alt="" />
          </div>
          <div className="cards-row">
            <img src={card} alt="" />
            <img src={card} alt="" />
          </div>
        </div>
      </div>
    )
  }
}

export default Deck
