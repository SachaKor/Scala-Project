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
    const { email, password } = values
    const { dispatch } = this.props

    dispatch(userActions.login(email, password))
    setSubmitting(false)
  }

  render() {
    return (
      <div className="identification-form-root">
        <div className="identification-form-container px-3 py-4">
          <Formik
            initialValues={{ email: '', password: '' }}
            validate={(values) => {
              const errors = {}
              if (!values.email) {
                errors.email = 'Champ obligatoire'
              } else if (
                !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(values.email)
              ) {
                errors.email = 'Adresse email invalide'
              }
              if (!values.password) {
                errors.password = 'Champ obligatoire'
              }

              return errors
            }}
            onSubmit={(values, { setSubmitting }) => {
              setTimeout(() => {
                alert(JSON.stringify(values, null, 2))
                setSubmitting(false)
              }, 400)
            }}
          >
            {({ isSubmitting, errors, touched }) => (
              <Form>
                <h1 className="mb-3 mt-0">Connexion</h1>
                <div className="mb-2">
                  <div className="input-title mb-1">Email</div>
                  <Field
                    type="email"
                    name="email"
                    className={errors.email && touched.email ? 'input-error' : ''}
                  />
                  <ErrorMessage
                    name="email"
                    component="div"
                    className="txt-error"
                  />
                </div>
                <div className="mb-3">
                  <div className="input-title mb-1">Mot de passe</div>
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
                    Se connecter
                  </button>
                </div>
                <div className="txt-info">
                  Pas encore de compte ?&nbsp;
                  <Link to={PATHS.SIGNIN}>S&apos;inscrire</Link>
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
