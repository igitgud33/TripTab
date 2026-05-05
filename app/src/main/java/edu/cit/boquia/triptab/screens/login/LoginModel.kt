package edu.cit.boquia.triptab.screens.login
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import edu.cit.boquia.triptab.app.CustomApp

class LoginModel(private val app: CustomApp) {
    // all SharedPref-related code now transferred in Model
    private var sharedPref = app.getSharedPreferences("TripTabPrefs", MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean("IS_LOGGED_IN", false)
    }

    fun setLoggedIn(status: Boolean) {
        sharedPref.edit {putBoolean("IS_LOGGED_IN", status).apply()}
    }


    fun validateCredentials(
        inputEmail: String,
        inputPassword: String
    ): Boolean {
        val savedEmail = sharedPref.getString("EMAIL", "")
        val savedPassword = sharedPref.getString("PASSWORD", "")

        return inputEmail == savedEmail && inputPassword == savedPassword
    }

    fun checkEmptyField(inputEmail: String?, inputPassword: String?): Boolean {
        return inputEmail.isNullOrBlank() || inputPassword.isNullOrBlank()
    }


}