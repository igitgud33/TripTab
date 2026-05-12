package edu.cit.boquia.triptab.app

import android.app.Application
import android.util.Log
import edu.cit.boquia.triptab.data.User

// holds data to be shared between activities
class CustomApp: Application() {

    val loginUser = User()

    override fun onCreate() {
        super.onCreate()
        Log.e("Custom App", "onCreate called")
    }
}