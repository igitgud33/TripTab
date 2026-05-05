package edu.cit.boquia.triptab.screens.profile

import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.data.User
import edu.cit.boquia.triptab.utils.getEditTextValue

class ProfileModel(private val app: CustomApp) {
    private val sharedPref = app.getSharedPreferences("TripTabPrefs", MODE_PRIVATE)

    fun getUserData(): User {
        return User(
            name = sharedPref.getString("NAME", "")?: "",
            email = sharedPref.getString("EMAIL", "")?: "",
            birthDate = sharedPref.getString("BIRTHDATE", "")?: "",
            phoneNo = sharedPref.getString("PHONE", "")?: "",
        )
    }

    fun clearSession() {
        sharedPref.edit {
            clear()
            apply()
        }
    }
}