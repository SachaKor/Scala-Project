import { api } from '../helpers'

export default {
  login,
  logout,
  register,
  getById,
  update,
  delete: _delete,
}

function login(username, password) {
  console.log( { username, password })
  return api
    .post('/users/login', { username, password })
    .then((response) => {
      // store user details and jwt token in local storage to keep user logged in between page refreshes
      localStorage.setItem('user', JSON.stringify(response.data.user))
      return response
    })
    .catch(handleError)
}

function logout() {
  // remove user from local storage to log user out
  localStorage.removeItem('user')
}

function getById(id) {
  return api.get(`/users/${id}`).catch(handleError)
}

function register(user) {
  return api.post('/users/signup', user).catch(handleError)
}

function update(user) {
  return api.put(`/users/${user.id}`, user).catch(handleError)
}

// prefixed function name with underscore because delete is a reserved word in javascript
function _delete(id) {
  return api.delete(`/users/${id}`).catch(handleError)
}

function handleError(error) {
  logout()
  return Promise.reject(error)
}
