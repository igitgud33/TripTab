package edu.cit.boquia.triptab.screens.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.data.User
import edu.cit.boquia.triptab.screens.summary.SummaryActivity
import edu.cit.boquia.triptab.screens.login.LoginActivity
import edu.cit.boquia.triptab.screens.main.MainActivity

class ProfileActivity : AppCompatActivity(), ProfileContract.View {
    private lateinit var profilePresenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // init presenter
        profilePresenter = ProfilePresenter(this, ProfileModel(application as CustomApp))

        // to display data from MainActivity
        profilePresenter.loadUserData()

        val btnProfileLogOut = findViewById<Button>(R.id.btnProfileLogOut)
        btnProfileLogOut.setOnClickListener {
            // clears profile info from shared preferences

            profilePresenter.logOut()

        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_profile)
        bottomNav.selectedItemId = R.id.nav_profile // highlights profile icon

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // custom transition animation (FYI: METHOD USED HERE IS DEPRECATED IN CURRENT VERSION)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }
                R.id.nav_summary -> {
                    val intent = Intent(this, SummaryActivity::class.java)
                    startActivity(intent)

                    // custom transition animation (FYI: METHOD USED HERE IS DEPRECATED IN CURRENT VERSION)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }

                R.id.nav_profile -> {
                    true
                }
                else -> false
            }
        }

    }

    override fun displayUserData(user: User) {
        val etName = findViewById<EditText>(R.id.etProfileName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etBirthDate = findViewById<EditText>(R.id.etBirthDate)
        val etPhoneNo = findViewById<EditText>(R.id.etPhoneNumber)

        etName.setText(user.name)
        etEmail.setText(user.email)
        etBirthDate.setText(user.birthDate)
        etPhoneNo.setText(user.phoneNo)
    }

    override fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)

        // clear back stack so user stays in Login
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }

}