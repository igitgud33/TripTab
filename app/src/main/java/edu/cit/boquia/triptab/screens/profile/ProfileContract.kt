package edu.cit.boquia.triptab.screens.profile

import edu.cit.boquia.triptab.data.User

interface ProfileContract {
    interface View {
        fun displayUserData(user: User)
        fun toLogin()
    }

    interface Presenter {
        fun loadUserData()
        fun updateProfile(user: User)
        fun logOut()
    }
}