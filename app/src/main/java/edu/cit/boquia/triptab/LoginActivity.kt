package edu.cit.boquia.triptab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.net.toUri
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // checks if user has logged in or out of the device
        val sharedPref = getSharedPreferences("TripTabPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("IS_LOGGED_IN", false)

        // immediately sends user to MainActivity if true
        if(isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //SharedPreferences - obtains info from SharedPreferences in Register
        val savedEmail = sharedPref.getString("EMAIL", "")
        val savedPassword = sharedPref.getString("PASSWORD", "")
        val savedName = sharedPref.getString("NAME", "")


        val etEmail = findViewById<EditText>(R.id.loginEmail)
        val etPassword = findViewById<EditText>(R.id.loginPassword)

        // Display the passed email and password
        etEmail.setText(savedEmail)
        etPassword.setText(savedPassword)

        // Sign Up White Button
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //  Proof of data passing via name (TEMPORARY ONLY)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text = if (!savedName.isNullOrBlank()) {
            "Hello, $savedName!"
        } else {
            "Ready to get started?"
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            // new logic on obtaining & displaying email & password
            val inputEmail = etEmail.text.toString()
            val inputPassword = etPassword.text.toString()

            if(inputEmail == savedEmail && inputPassword == savedPassword) {
                val editor = sharedPref.edit()
                editor.putBoolean("IS_LOGGED_IN", true)
                editor.apply()

                // Explicit Intent
                val intent = Intent(this, MainActivity::class.java)

                // wipe out previous nav history and starts a new one w/ MainActivity as starting pt
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)

                // removes login from backstack and prevents going back to login
                finish()
            }
        }

        // Implicit Intents
        val btnGoogle = findViewById<Button>(R.id.btnGoogle)
        btnGoogle.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://accounts.google.com/signup".toUri()
            startActivity(intent)
        }

        val btnFacebook = findViewById<Button>(R.id.btnFacebook)
        btnFacebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://www.facebook.com/".toUri()
            startActivity(intent)
        }
    }
}