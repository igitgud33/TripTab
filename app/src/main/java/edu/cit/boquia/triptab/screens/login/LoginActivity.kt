package edu.cit.boquia.triptab.screens.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import edu.cit.boquia.triptab.screens.main.MainActivity
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.screens.register.RegisterActivity
import edu.cit.boquia.triptab.utils.getEditTextValue
import edu.cit.boquia.triptab.utils.toast

class LoginActivity : AppCompatActivity(), LoginContract.View {

    // initialize LoginPresenter
    private lateinit var loginPresenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // initialize presenter
        loginPresenter = LoginPresenter(this, LoginModel(application as CustomApp))

        // checks if user has logged in or out of the device via presenter
        loginPresenter.checkLoginStatus()

        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            toRegister()
        }


        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val inputEmail = getEditTextValue(R.id.loginEmail)
            val inputPassword = getEditTextValue(R.id.loginPassword)

            loginPresenter.validateLogin(inputEmail, inputPassword)

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


    // Functions from interface (most functional code blocks placed here)
    override fun onSuccess() {
        toast("Login successful!")
    }

    override fun onInvalid() {
        toast("Invalid credentials. Please try again.")
    }

    override fun onEmpty() {
        toast("Please fill in the missing fields.")
    }

    override fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finish()
    }

    override fun toRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}