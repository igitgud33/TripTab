package edu.cit.boquia.triptab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.net.toUri

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //login input data
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val birthDate = intent.getStringExtra("birthDate")
        val phoneNo = intent.getStringExtra("phoneNo")
        val password = intent.getStringExtra("password")

        val etEmail = findViewById<EditText>(R.id.loginEmail)
        val etPassword = findViewById<EditText>(R.id.loginPassword)

        // Display the passed email and password
        etEmail.setText(email)
        etPassword.setText(password)


        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //  Proof of data passing via name (TEMPORARY ONLY)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text =  if (name != null) {
            "Hello, $name!"
        } else {
            "Hello!"
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            // Explicit Intent
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("email", email)
            intent.putExtra("birthDate", birthDate)
            intent.putExtra("phoneNo", phoneNo)

            // wipe out previous nav history and starts a new one w/ MainActivity as starting pt
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            // removes login from backstack and prevents going back to login
            finish()
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