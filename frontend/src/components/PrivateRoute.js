import React from 'react'
import PropTypes from 'prop-types'
import { Redirect, Route } from 'react-router-dom'

const PrivateRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={(props) =>
      localStorage.getItem('user') ? (
        <Component {...props} />
      ) : (
        <Redirect
          to={{ pathname: '/login', state: { from: props.location } }}
        />
      )
    }
  />
)

PrivateRoute.propTypes = {
  location: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
    .isRequired,
  component: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.element,
    PropTypes.node,
  ]).isRequired,
}

export default PrivateRoute
