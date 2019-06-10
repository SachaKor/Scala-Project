import React, { Component } from 'react'
import SocketContext from '../../utils/SocketContext'

import card from '../../assets/cards/back.png'

import './Deck.scss'

const INITIAL_STATE = {

}

class Deck extends Component {
  constructor(props) {
    super(props)
    this.state = INITIAL_STATE
  }

  componentDidMount() {
    const height = this.container.clientHeight
    this.setState({ height })
  }

  handleClick = (i) => {
    this.props.socket.send(JSON.stringify({
      eventType: "cardClick",
      eventContent: {
        index: i,
        name: this.props.name,
        deck: "nope"
      }
    }))
  }

  handleNothing = () => {
    console.log('Denied')
  }

  render() {
    const { rotate, cards } = this.props
    let className = ""
    className += rotate === 1 ? " top-deck" : ""
    className += rotate === 2 ? " left-deck" : ""
    className += rotate === 3 ? " right-deck" : ""

    let width = 0
    if(this.state.height)
      width = (this.state.height / 2 * 0.7) * Math.ceil(this.props.cards.length / 2)

    return (
      <div className={"deck" + className} ref={(div) => this.container = div} style={{width: width}}>
        { cards.map((c, i) =>
          <div className="img-container" key={`${i}${c.rank}${c.suit}`}>
            <img src={require(`./PNG/${c.rank}${c.suit}.png`)} alt="product" onClick={() => this.handleClick(i)} />
          </div>
        )}
      </div>
    )
  }
}

export default Deck
