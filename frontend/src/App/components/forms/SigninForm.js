import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'
import { userActions } from '../../../actions'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import { PATHS } from '../../routes'

import './Forms.scss'

class SigninForm extends Component {
  handleSubmit = (values, { setSubmitting }) => {
    const { email, password, username } = values
    const { dispatch } = this.props

    const user = {
      email,
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
              email: '',
              password: '',
              password2: '',
              pseudonym: '',
            }}
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
              if (!values.password2) {
                errors.password2 = 'Champ obligatoire'
              } else if (values.password2 !== values.password) {
                errors.password2 =
                  'Les deux mots de passes doivent être identiques'
              }
              if (!values.pseudonym) {
                errors.pseudonym = 'Champ obligatoire'
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
                <h1 className="mb-3 mt-0">Inscription</h1>
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
                <div className="mb-2">
                  <div className="input-title mb-1">Nom d&apos;utilisateur</div>
                  <Field
                    type="text"
                    name="pseudonym"
                    className={
                      errors.pseudonym && touched.pseudonym ? 'input-error' : ''
                    }
                  />
                  <ErrorMessage
                    name="pseudonym"
                    component="div"
                    className="txt-error"
                  />
                </div>
                <div className="mb-2">
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
                <div className="mb-3">
                  <div className="input-title mb-1">Répétez le mot de passe</div>
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
                    S&apos;inscrire
                  </button>
                </div>
                <div className="txt-info">
                  <Link to={PATHS.LOGIN}>Se connecter</Link>
                </div>
              </Form>
            )}
          </Formik>
        </div>
      </div>
    )
  }
}

SigninForm.propTypes = {
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

const connectedSigninForm = connect(mapStateToProps)(SigninForm)

export default connectedSigninForm
