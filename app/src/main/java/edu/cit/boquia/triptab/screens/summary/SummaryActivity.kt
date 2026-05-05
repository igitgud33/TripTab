package edu.cit.boquia.triptab.screens.summary

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.screens.main.MainActivity
import edu.cit.boquia.triptab.screens.profile.ProfileActivity

class SummaryActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary)

        // Retrieve data from intent
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val birthDate = intent.getStringExtra("birthDate")
        val phoneNo = intent.getStringExtra("phoneNo")


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_summary)
        bottomNav.selectedItemId = R.id.nav_summary // highlights summary icon
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }
                R.id.nav_summary -> {
                    true
                }

                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }

                else -> false
            }
        }

    }

}