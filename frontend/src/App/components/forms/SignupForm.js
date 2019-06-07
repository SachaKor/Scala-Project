import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'
import { userActions } from '../../../actions'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import { PATHS } from '../../routes'

import './Forms.scss'

class SignupForm extends Component {
  handleSubmit = (values, { setSubmitting }) => {
    const { password, username } = values
    const { dispatch } = this.props

    const user = {
      username,
      password,
    }
    dispatch(userActions.register(user))
    setSubmitting(false)
  }

  render() {
    return (
      <div className="identification-form-root">
        <div className="identification-form-container px-3 py-4">
          <Formik
            initialValues={{
              password: '',
              password2: '',
              username: '',
            }}
            validate={(values) => {
              const errors = {}
              if (!values.password) {
                errors.password = 'Required'
              }
              if (!values.password2) {
                errors.password2 = 'Required'
              } else if (values.password2 !== values.password) {
                errors.password2 =
                  'Password must be identical'
              }
              if (!values.username) {
                errors.username = 'Required'
              }

              return errors
            }}
            onSubmit={this.handleSubmit}
          >
            {({ isSubmitting, errors, touched }) => (
              <Form>
                <h1 className="mb-3 mt-0">Signup</h1>
                <div className="mb-2">
                  <div className="input-title mb-1">Username</div>
                  <Field
                    type="text"
                    name="username"
                    className={
                      errors.username && touched.username ? 'input-error' : ''
                    }
                  />
                  <ErrorMessage
                    name="username"
                    component="div"
                    className="txt-error"
                  />
                </div>
                <div className="mb-2">
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
                <div className="mb-3">
                  <div className="input-title mb-1">Repeat password</div>
                  <Field
                    type="password"
                    name="password2"
                    className={
                      errors.password2 && touched.password2 ? 'input-error' : ''
                    }
                  />
                  <ErrorMessage
                    name="password2"
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
                    Signup
                  </button>
                </div>
                <div className="txt-info">
                  <Link to={PATHS.LOGIN}>Login</Link>
                </div>
              </Form>
            )}
          </Formik>
        </div>
      </div>
    )
  }
}

SignupForm.propTypes = {
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

const connectedSignupForm = connect(mapStateToProps)(SignupForm)

export default connectedSignupForm
