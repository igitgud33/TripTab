package edu.cit.boquia.triptab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // to get data from MainActivity
        val sharedPref = getSharedPreferences("TripTabPrefs", MODE_PRIVATE)

        val name = sharedPref.getString("NAME", "")
        val email = sharedPref.getString("EMAIL", "")
        val birthDate = sharedPref.getString("BIRTHDATE", "")
        val phoneNo = sharedPref.getString("PHONE", "")

        // to get edit text ids from activity_profile.xml
        val etName = findViewById<EditText>(R.id.etProfileName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etBirthDate = findViewById<EditText>(R.id.etBirthDate)
        val etPhoneNo = findViewById<EditText>(R.id.etPhoneNumber)

        // display data
        etName.setText(name ?: "")
        etEmail.setText(email ?: "")
        etBirthDate.setText(birthDate ?: "")
        etPhoneNo.setText(phoneNo ?: "")

        val btnProfileLogOut = findViewById<Button>(R.id.btnProfileLogOut)
        btnProfileLogOut.setOnClickListener {
            // clears profile info from shared preferences
            val editor = sharedPref.edit()

            editor.clear()
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)

            // clear back stack so user stays in Login
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
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

}