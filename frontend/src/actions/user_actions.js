import { userConstants } from '../constants'
import userServices from '../services'
import { alertActions } from '.'
import { history } from '../helpers'

export default {
  login,
  logout,
  register,
  delete: _delete,
}

function login(username, password) {
  return (dispatch) => {
    dispatch(request({ username }))

    userServices.login(username, password).then(
      (user) => {
        dispatch(success(user))
        history.push('/board')
        dispatch(alertActions.success('Login successful'))
      },
      (error) => {
        dispatch(failure(error.message))
        dispatch(alertActions.error(error.message))
      }
    )
  }

  function request(user) {
    return { type: userConstants.LOGIN_REQUEST, user }
  }
  function success(user) {
    return { type: userConstants.LOGIN_SUCCESS, user }
  }
  function failure(error) {
    return { type: userConstants.LOGIN_FAILURE, error }
  }
}

function logout() {
  userServices.logout()
  return { type: userConstants.LOGOUT }
}

function register(user) {
  return (dispatch) => {
    dispatch(request(user))

    userServices.register(user).then(
      (user) => {
        dispatch(success())
        history.push('/login')
        dispatch(alertActions.success('Registration successful'))
      },
      (error) => {
        dispatch(failure(error.message))
        dispatch(alertActions.error(error.message))
      }
    )
  }

  function request(user) {
    return { type: userConstants.REGISTER_REQUEST, user }
  }
  function success(user) {
    return { type: userConstants.REGISTER_SUCCESS, user }
  }
  function failure(error) {
    return { type: userConstants.REGISTER_FAILURE, error }
  }
}

// prefixed function name with underscore because delete is a reserved word in javascript
function _delete(id) {
  return (dispatch) => {
    dispatch(request(id))

    userServices
      .delete(id)
      .then(
        (user) => dispatch(success(id)),
        (error) => dispatch(failure(id, error.response.data))
      )
  }

  function request(id) {
    return { type: userConstants.DELETE_REQUEST, id }
  }
  function success(id) {
    return { type: userConstants.DELETE_SUCCESS, id }
  }
  function failure(id, error) {
    return { type: userConstants.DELETE_FAILURE, id, error }
  }
}
