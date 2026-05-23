package edu.cit.boquia.triptab.screens.profile

import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.data.User
import edu.cit.boquia.triptab.utils.getEditTextValue

class ProfileModel(private val app: CustomApp) {
    private val sharedPref = app.getSharedPreferences("TripTabPrefs", MODE_PRIVATE)

    fun saveUserData(user: User) {
        sharedPref.edit {
            putString("NAME", user.name)
            putString("EMAIL", user.email)
            putString("BIRTHDATE", user.birthDate)
            putString("PHONE", user.phoneNo)
            putString("PASSWORD", user.password)
            putString("PROFILE_URI", user.profileImageUri)
            apply()
        }
    }

    fun getUserData(): User {
        return User(
            name = sharedPref.getString("NAME", "") ?: "",
            email = sharedPref.getString("EMAIL", "") ?: "",
            birthDate = sharedPref.getString("BIRTHDATE", "") ?: "",
            phoneNo = sharedPref.getString("PHONE", "") ?: "",
            password = sharedPref.getString("PASSWORD", "") ?: "",
            profileImageUri = sharedPref.getString("PROFILE_URI", null),

        )
    }

    fun clearSession() {
        sharedPref.edit {
            remove("IS_LOGGED_IN") // sets user to logged out instead of wiping data entirely
            apply()
        }
    }
}