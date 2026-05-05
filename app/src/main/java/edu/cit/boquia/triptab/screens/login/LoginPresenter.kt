package edu.cit.boquia.triptab.screens.login

import android.app.Activity
import edu.cit.boquia.triptab.app.CustomApp

class LoginPresenter(
    private var view: LoginContract.View,
    private val model: LoginModel
) : LoginContract.Presenter {

    override fun checkLoginStatus() {
        if (model.isLoggedIn()) {
            view.toMain()
        }
    }

    override fun validateLogin(
        inputEmail: String,
        inputPassword: String
    ) {
        if (model.checkEmptyField(inputEmail, inputPassword)) {
            view.onEmpty()
            return
        }

        if (model.validateCredentials(inputEmail, inputPassword)) {
            // save session via model
            model.setLoggedIn(true)

            //saves info to global user

            view.onSuccess()
            view.toMain()
        } else {
            view.onInvalid()
        }
    }

}
