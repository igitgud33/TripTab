package edu.cit.boquia.triptab.screens.profile

import edu.cit.boquia.triptab.data.User

class ProfilePresenter(
    private var view: ProfileContract.View,
    private val model: ProfileModel
): ProfileContract.Presenter {

    override fun loadUserData() {
        val user = model.getUserData()
        view.displayUserData(user)
    }

    override fun updateProfile(user: User) {
        model.saveUserData(user)
        view.displayUserData(user) // view refresh
    }

    override fun logOut() {
        model.clearSession()
        view.toLogin()
    }

}