package edu.cit.boquia.triptab.screens.register

import android.widget.EditText
import edu.cit.boquia.triptab.data.User

class RegisterContract {
    interface View {
        fun onSuccess()
        fun onEmpty()
        fun onInvalid()
        fun toLogin()
        fun showDatePicker(etBirthDate: EditText)
    }

    interface Presenter {
        fun registerUser(user: User)
    }
}