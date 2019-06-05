import axios from 'axios'

const user = JSON.parse(localStorage.getItem('user'))
const authorization = user && user.token ? user.token : ''

export default axios.create({
  baseURL:
    process.env.NODE_ENV !== 'production' ? 'http://localhost:3001/' : '',
  headers: {
    Authorization: `${authorization}`,
    'content-type': 'application/json',
  },
})
