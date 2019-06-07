import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Link } from 'react-router-dom'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import { connect } from 'react-redux'
import { userActions } from '../../../actions'
import { PATHS } from '../../routes'

import './Forms.scss'

class LoginForm extends Component {
  constructor(props) {
    super(props)

    // reset login status
    this.props.dispatch(userActions.logout())
  }

  handleSubmit = (values, { setSubmitting }) => {
    const { username, password } = values
    const { dispatch } = this.props

    dispatch(userActions.login(username, password))
    setSubmitting(false)
  }

  render() {
    return (
      <div className="identification-form-root">
        <div className="identification-form-container px-3 py-4">
          <Formik
            initialValues={{ username: '', password: '' }}
            validate={(values) => {
              const errors = {}
              if (!values.username) {
                errors.username = 'Required'
              }
              if (!values.password) {
                errors.password = 'Required'
              }

              return errors
            }}
            onSubmit={this.handleSubmit}
          >
            {({ isSubmitting, errors, touched }) => (
              <Form>
                <h1 className="mb-3 mt-0">Login</h1>
                <div className="mb-2">
                  <div className="input-title mb-1">Username</div>
                  <Field
                    type="username"
                    name="username"
                    className={errors.username && touched.username ? 'input-error' : ''}
                  />
                  <ErrorMessage
                    name="username"
                    component="div"
                    className="txt-error"
                  />
                </div>
                <div className="mb-3">
                  <div className="input-title mb-1">Password</div>
                  <Field
                    type="password"
                    name="password"
                    className={
                      errors.password && touched.password ? 'input-error' : ''
                    }
                  />
                  <ErrorMessage
                    name="password"
                    component="div"
                    className="txt-error"
                  />
                </div>
                <div className="btn-container mb-1">
                  <button
                    type="submit"
                    disabled={isSubmitting}
                    className="generic-btn"
                  >
                    Login
                  </button>
                </div>
                <div className="txt-info">
                  No account yet ?&nbsp;
                  <Link to={PATHS.SIGNUP}>Signup</Link>
                </div>
              </Form>
            )}
          </Formik>
        </div>
      </div>
    )
  }
}

LoginForm.propTypes = {
  dispatch: PropTypes.func.isRequired,
  alert: PropTypes.shape({
    message: PropTypes.string,
  }).isRequired,
}

function mapStateToProps(state) {
  const { loggingIn } = state.authentication
  const { alert } = state

  return {
    loggingIn,
    alert,
  }
}

const connectedLoginForm = connect(mapStateToProps)(LoginForm)

export default connectedLoginForm
