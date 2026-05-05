package edu.cit.boquia.triptab.screens.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.boquia.triptab.screens.profile.ProfileActivity
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.screens.login.LoginPresenter
import edu.cit.boquia.triptab.screens.summary.SummaryActivity
import edu.cit.boquia.triptab.screens.plan.PlanActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnPlan = findViewById<Button>(R.id.btnCreateNewPlan)
        btnPlan.setOnClickListener {
            val intent = Intent(this, PlanActivity::class.java)
            startActivity(intent)
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_home // highlights home icon

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
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