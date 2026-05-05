package edu.cit.boquia.triptab.screens.register

import edu.cit.boquia.triptab.data.User

class RegisterPresenter(
    private var view: RegisterContract.View,
    private var model: RegisterModel
): RegisterContract.Presenter {

    override fun registerUser(user: User) {
        // validate data
        if(model.validateRegistration(user)) {
            // save to local if valid
            model.saveToLocal(user)

            // proceed to login
            view.onSuccess()
            view.toLogin()
        } else {
            view.onInvalid()
        }
    }

}