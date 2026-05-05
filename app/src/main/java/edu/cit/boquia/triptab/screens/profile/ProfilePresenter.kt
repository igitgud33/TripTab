package edu.cit.boquia.triptab.screens.profile

class ProfilePresenter(
    private var view: ProfileContract.View,
    private val model: ProfileModel
): ProfileContract.Presenter {

    override fun loadUserData() {
        val user = model.getUserData()
        view.displayUserData(user)
    }

    override fun logOut() {
        model.clearSession()
        view.toLogin()
    }

}