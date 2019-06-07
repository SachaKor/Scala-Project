import axios from 'axios'

const user = JSON.parse(localStorage.getItem('user'))
const authorization = user && user.token ? user.token : ''

export default axios.create({
  baseURL: 'http://localhost:9000/',
  headers: {
    Authorization: `${authorization}`,
    'content-type': 'application/json',
  },
})
