package edu.cit.boquia.triptab.screens.register

import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.data.User

class RegisterModel(private val app: CustomApp) {

    private var sharedPref = app.getSharedPreferences("TripTabPrefs", MODE_PRIVATE)

    fun validateRegistration(user: User): Boolean {
        return user.name.isNotEmpty() &&
                user.email.isNotEmpty() &&
                user.birthDate.isNotEmpty() &&
                user.phoneNo.isNotEmpty() &&
                user.password.isNotEmpty()
    }

    fun saveToLocal(user: User) {
        sharedPref.edit {
            putString("NAME", user.name)
            putString("EMAIL", user.email)
            putString("BIRTHDATE", user.birthDate)
            putString("PHONE", user.phoneNo)
            putString("PASSWORD", user.password)

            apply()
        }
    }

}