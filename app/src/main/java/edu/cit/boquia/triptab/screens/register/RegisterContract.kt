package edu.cit.boquia.triptab.screens.register

import android.widget.EditText
import edu.cit.boquia.triptab.data.User

interface RegisterContract {
    interface View {
        fun onSuccess()
        fun onEmpty()
        fun onInvalid()
        fun toLogin()
    }

    interface Presenter {
        fun registerUser(user: User)
    }
}