package edu.cit.boquia.triptab.screens.plan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.boquia.triptab.screens.main.MainActivity
import edu.cit.boquia.triptab.screens.profile.ProfileActivity
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.screens.summary.SummaryActivity

class PlanActivity : AppCompatActivity() {

    // Late initializations for Expense Rows
    private lateinit var expensesContainer: LinearLayout
    private lateinit var totalBudget: EditText
    private lateinit var totalExpenses: EditText
    private lateinit var remainingBudget: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        // Initialize views
        expensesContainer = findViewById(R.id.expensesContainer)
        totalBudget = findViewById(R.id.totalBudget)
        totalExpenses = findViewById(R.id.totalExpenses)
        remainingBudget = findViewById(R.id.remainingBudget)

        // Add Expense Button
        val btnAddExpense = findViewById<Button>(R.id.btnAddExpense)
        btnAddExpense.setOnClickListener {
            addNewExpenseRow()
        }

        // Back Button
        val btnPlanBack = findViewById<ImageButton>(R.id.btnPlanBack)
        btnPlanBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.accelerate_interpolator, android.R.anim.decelerate_interpolator)
        }

        // Save Plan
        val btnSavePlan = findViewById<Button>(R.id.btnSavePlan)
        btnSavePlan.setOnClickListener {
            savePlan()

        }


        setupBottomNavigation()
    }

    // Function that adds new expense row container using expense_row.xml
    private fun addNewExpenseRow() {
        // Creates LayoutInflater to inflate expense_row.xml into the container in plan.xml
        val inflater = LayoutInflater.from(this)
        val rowView = inflater.inflate(R.layout.expense_row, null)

        // Find delete button in expense row
        val btnDelete = rowView.findViewById<ImageButton>(R.id.btnDeleteExpense)

        btnDelete.setOnClickListener {
            expensesContainer.removeView(rowView)
            calculateTotals() // Update math when removed
        }

        // Adds row to activity_plan.xml
        expensesContainer.addView(rowView)
    }

    private fun calculateTotals() {
        // math logic
    }
    // actual function to save the plan
    private fun savePlan() {
        val name = findViewById<EditText>(R.id.planName).text.toString()
        val desc = findViewById<EditText>(R.id.planDescription).text.toString()
        val dest = findViewById<EditText>(R.id.planDestination).text.toString()
        val start = findViewById<EditText>(R.id.planStartDate).text.toString()
        val end = findViewById<EditText>(R.id.planEndDate).text.toString()
        val budget = findViewById<EditText>(R.id.totalBudget).text.toString().toDoubleOrNull() ?: 0.0

        // expense rows
        val expenseList = mutableListOf<Expense>()
        for(i in 0 until expensesContainer.childCount) {
            val row = expensesContainer.getChildAt(i)

            //row.findViewById to collect row data
            val expDesc = row.findViewById<EditText>(R.id.expenseDescription).text.toString()
            val expAmount = row.findViewById<EditText>(R.id.expenseAmount).text.toString().toDoubleOrNull()?: 0.0
            val expDate = row.findViewById<EditText>(R.id.expenseDate).text.toString()

            expenseList.add(Expense(expDesc, expAmount, expDate))
        }

        // create a new plan
        val newPlan = Plan(
            id = System.currentTimeMillis().toString(),
            name = name,
            destination = dest,
            description = desc,
            startDate = start,
            endDate = end,
            totalBudget = budget,
            expenses = expenseList
        )
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_home

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
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    true
                }
                else -> false
            }
        }
    }
}