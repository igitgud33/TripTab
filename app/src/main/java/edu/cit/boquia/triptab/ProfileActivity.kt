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
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val birthDate = intent.getStringExtra("birthDate")
        val phoneNo = intent.getStringExtra("phoneNo")

        // to get edit text ids from activity_profile.xml
        val etName = findViewById<EditText>(R.id.etProfileName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etBirthDate = findViewById<EditText>(R.id.etBirthDate)
        val etPhoneNo = findViewById<EditText>(R.id.etPhoneNumber)

        val btnProfileLogOut = findViewById<Button>(R.id.btnProfileLogOut)
        btnProfileLogOut.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // display data
        etName.setText(name ?: "")
        etEmail.setText(email ?: "")
        etBirthDate.setText(birthDate ?: "")
        etPhoneNo.setText(phoneNo ?: "")

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_profile)
        bottomNav.selectedItemId = R.id.nav_profile // highlights profile icon

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }
                R.id.nav_summary -> {
                    startActivity(Intent(this, SummaryActivity::class.java))
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