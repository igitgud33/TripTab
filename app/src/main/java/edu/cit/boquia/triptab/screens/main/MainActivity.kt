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
import edu.cit.boquia.triptab.screens.plan.PlanGroup
import edu.cit.boquia.triptab.screens.plan.PlanModel
import edu.cit.boquia.triptab.utils.toast
import kotlin.collections.forEach

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var mainPresenter: MainContract.Presenter
    private lateinit var planModel: PlanModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        planModel = PlanModel(application as CustomApp)
        mainPresenter = MainPresenter(this, planModel)


        val btnPlan = findViewById<Button>(R.id.btnCreateNewPlan)
        btnPlan.setOnClickListener {
           toCreatePlan()
        }

        // group button
        findViewById<Button>(R.id.btnCreateGroup).setOnClickListener {
            showCreateGroupDialog()
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
        // now considers both grouped & ungrouped
        val groups = planModel.getAllGroups()

        val groupedContainer = findViewById<LinearLayout>(R.id.containerGroupedPlans)
        val ungroupedContainer = findViewById<LinearLayout>(R.id.containerUngroupedPlans)

        groupedContainer.removeAllViews()
        ungroupedContainer.removeAllViews()

        // find plan IDs already in group
        val allGroupedIds = groups.flatMap { it.planIds }.toSet()

        // grouped plans display
        if(groups.isNotEmpty()) {
            findViewById<TextView>(R.id.tvNoGroupedPlans).visibility = android.view.View.GONE
            groups.forEach { group ->
                // inflate header
                val groupHeader = layoutInflater.inflate(R.layout.item_group_header, groupedContainer, false)
                groupHeader.findViewById<TextView>(R.id.tvGroupName).text = group.name

                // for deleting group
                groupHeader.findViewById<Button>(R.id.btnDeleteGroup).setOnClickListener {
                    planModel.deleteGroup(group.id)
                    mainPresenter.loadDashboard()
                    toast("Group ${group.name} dissolved")
                }

                groupedContainer.addView(groupHeader)

                // add cards belonging to this group
                plans.filter {it.id in group.planIds}.forEach { plan ->
                    val card = createPlanCard(plan, groupedContainer)
                    groupedContainer.addView(card)
                }

            }
        }

        // ungrouped plans
        val ungroupedPlans = plans.filter {it.id !in allGroupedIds}
        if(ungroupedPlans.isNotEmpty()) {
            findViewById<TextView>(R.id.tvNoUngroupedPlans).visibility = android.view.View.GONE
            ungroupedPlans.forEach { plan ->
                val card = createPlanCard(plan, ungroupedContainer)
                ungroupedContainer.addView(card)
            }
        } else {
            findViewById<TextView>(R.id.tvNoUngroupedPlans).visibility = android.view.View.VISIBLE
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

    // all card operations placed here instead
    private fun createPlanCard(plan: Plan, parent: android.view.ViewGroup): android.view.View {
        // inflate card layout
        val card = layoutInflater.inflate(R.layout.item_plan, parent, false)

        // progress bar logic
        val spent = plan.expenses.sumOf { it.amount }
        val remaining = plan.totalBudget - spent
        val progressPercent = if (plan.totalBudget > 0.0) ((spent / plan.totalBudget) * 100).toInt() else 0


        // bind cards
        card.findViewById<TextView>(R.id.tvPlanName).text = plan.name

        // budgets
        card.findViewById<TextView>(R.id.tvRemainingBudget).text = "Remaining Budget: $remaining"
        card.findViewById<TextView>(R.id.tvTotalBudget).text = "Total Budget: ${plan.totalBudget}"

        // dates
        card.findViewById<TextView>(R.id.tvStartingDate).text = "Starting Date: ${plan.startDate}"
        card.findViewById<TextView>(R.id.tvEndDate).text = "End Date: ${plan.endDate}"

        // pb
        val pb = card.findViewById<ProgressBar>(R.id.pbBudget)
        pb.progress = progressPercent



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
        return card
    }

    // dialog to let user pick plans for group
    private fun showCreateGroupDialog() {
        // check if plans have already been created
        val allPlans = planModel.getAllPlans()
        if(allPlans.isEmpty()) {
            toast("Create a plan first!")
            return
        }

        // init Builder that builds popup by piece
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Create New Group")

        // name input
        val input = android.widget.EditText(this)
        input.hint = "Group Name (e.g. Winter 1987)"
        builder.setView(input)

        // plan selection (creates checklist from extracted plan names)
        val planNames = allPlans.map {it.name}.toTypedArray()
        val selectedItems = BooleanArray(allPlans.size) {false}

        // updates checklist status based on user
        builder.setMultiChoiceItems(planNames, selectedItems) {_, which, isChecked ->
            selectedItems[which] = isChecked
        }

        // finds id of plans ticked True by user
        builder.setPositiveButton("Create") { _, _ ->
            val groupName = input.text.toString()
            val selectedPlanIds = allPlans.filterIndexed { index, _ -> selectedItems[index] }.map { it.id }

            // validation, create new Plan group w/ ID, and send to Model to be saved to sharedPref
            if(groupName.isNotEmpty() && selectedPlanIds.isNotEmpty()) {
                val newGroup = PlanGroup(
                    id = java.util.UUID.randomUUID().toString(),
                    name = groupName,
                    planIds = selectedPlanIds
                )
                planModel.saveGroup(newGroup)
                mainPresenter.loadDashboard() // refreshes list
                toast("Group ${groupName} created!")
            } else {
                toast("Please enter a name and select desired plans")
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

}