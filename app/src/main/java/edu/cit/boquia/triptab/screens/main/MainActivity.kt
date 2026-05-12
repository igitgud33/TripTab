package edu.cit.boquia.triptab.screens.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.boquia.triptab.screens.profile.ProfileActivity
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.screens.plan.Plan
import edu.cit.boquia.triptab.screens.summary.SummaryActivity
import edu.cit.boquia.triptab.screens.plan.PlanActivity
import edu.cit.boquia.triptab.screens.plan.PlanModel
import edu.cit.boquia.triptab.utils.toast
import kotlin.collections.forEach

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var mainPresenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mainPresenter = MainPresenter(this, PlanModel(application as CustomApp))


        val btnPlan = findViewById<Button>(R.id.btnCreateNewPlan)
        btnPlan.setOnClickListener {
           toCreatePlan()
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

    // override onResume (allows Dashboard to refresh everytime for Plan updates)
    override fun onResume() {
        super.onResume()
        // runs everytime user returns to this screen
        mainPresenter.loadDashboard()
    }

    override fun displayPlans(plans: List<Plan>) {
        val container = findViewById<LinearLayout>(R.id.containerUngroupedPlans)
        val tvEmpty = findViewById<TextView>(R.id.tvNoUngroupedPlans)

        // clear previous cards
        container.removeAllViews()

        // hide "No Plans" text
        tvEmpty.visibility = android.view.View.GONE

        // loop through each plan and make a card
        plans.forEach { plan ->
            // inflate card layout
            val card = layoutInflater.inflate(R.layout.item_plan, container, false)

            // progress bar logic
            val spent = plan.expenses.sumOf { it.amount }
            val remaining = plan.totalBudget - spent
            val progressPercent = if (plan.totalBudget > 0.0) {
                ((spent / plan.totalBudget) * 100).toInt()
            } else {
                0
            }

            // bind cards
            card.findViewById<TextView>(R.id.tvPlanName).text = plan.name

            // budgets
            card.findViewById<TextView>(R.id.tvRemainingBudget).text =
                "Remaining Budget: $remaining"
            card.findViewById<TextView>(R.id.tvTotalBudget).text =
                "Total Budget: ${plan.totalBudget}"

            // dates
            card.findViewById<TextView>(R.id.tvStartingDate).text =
                "Starting Date: ${plan.startDate}"
            card.findViewById<TextView>(R.id.tvEndDate).text = "End Date: ${plan.endDate}"

            // pb
            val pb = card.findViewById<ProgressBar>(R.id.pbBudget)
            pb.progress = progressPercent

            // add card to screen
            container.addView(card)


            // edit click
            card.setOnClickListener {
                val intent = Intent(this, PlanActivity::class.java)
                intent.putExtra("PLAN_ID", plan.id) // pass ID to next screen
                startActivity(intent)
            }

            // delete click
            card.findViewById<Button>(R.id.btnDeletePlan).setOnClickListener {
                mainPresenter.deletePlan(plan.id)
                mainPresenter.loadDashboard() // refresh list immediately

                toast("Plan successfully deleted!")
            }
        }


    }

    override fun showEmptyState() {
        val container = findViewById<LinearLayout>(R.id.containerUngroupedPlans)
        val tvEmpty = findViewById<TextView>(R.id.tvNoUngroupedPlans)

        container.removeAllViews()
        // show the "is empty" text
        tvEmpty.visibility = android.view.View.VISIBLE
    }

    override fun toCreatePlan() {
        val intent = Intent(this, PlanActivity::class.java)
        startActivity(intent)
    }

}