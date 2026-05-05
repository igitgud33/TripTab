package edu.cit.boquia.triptab.screens.login

class LoginContract {
    interface View {
        fun onSuccess()
        fun onInvalid()
        fun onEmpty()
        fun toMain()

        fun toRegister()
    }

    interface Presenter {
        // checks isLoggedIn
        fun checkLoginStatus()

        // user inputted
        fun validateLogin(inputEmail: String, inputPassword: String)
    }
}