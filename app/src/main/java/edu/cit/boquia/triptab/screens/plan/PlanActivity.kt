package edu.cit.boquia.triptab.screens.plan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.boquia.triptab.screens.main.MainActivity
import edu.cit.boquia.triptab.screens.profile.ProfileActivity
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.screens.summary.SummaryActivity
import edu.cit.boquia.triptab.utils.showDatePicker
import edu.cit.boquia.triptab.utils.toast

class PlanActivity : AppCompatActivity(), PlanContract.View {

    // Late initializations for Expense Rows
    private lateinit var expensesContainer: LinearLayout
    private lateinit var totalBudget: EditText
    private lateinit var totalExpenses: EditText
    private lateinit var remainingBudget: EditText

    private lateinit var planPresenter: PlanContract.Presenter
    private lateinit var planModel: PlanModel
    private var editingPlanId: String? = null // for Plans that may need editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plan)

        // Initialize views
        expensesContainer = findViewById(R.id.expensesContainer)
        totalBudget = findViewById(R.id.totalBudget)
        totalExpenses = findViewById(R.id.totalExpenses)
        remainingBudget = findViewById(R.id.remainingBudget)

        planModel = PlanModel(application as CustomApp)
        planPresenter = PlanPresenter(this, planModel)

        totalBudget.doAfterTextChanged {
            calculateTotals()
        }

        // date pickers
        val etStartDate = findViewById<EditText>(R.id.planStartDate)
        val etEndDate = findViewById<EditText>(R.id.planEndDate)

        etStartDate.setOnClickListener {
            showDatePicker(etStartDate)
        }

        etEndDate.setOnClickListener {
            showDatePicker(etEndDate)
        }

        // Add Expense Button
        val btnAddExpense = findViewById<Button>(R.id.btnAddExpense)
        btnAddExpense.setOnClickListener {
            addNewExpenseRow()
        }

        // Back Button
        val btnPlanBack = findViewById<ImageButton>(R.id.btnPlanBack)
        btnPlanBack.setOnClickListener {
            overridePendingTransition(android.R.anim.accelerate_interpolator, android.R.anim.decelerate_interpolator)
            finish()
        }

        // Save Plan
        val btnSavePlan = findViewById<Button>(R.id.btnSavePlan)
        btnSavePlan.setOnClickListener {
            savePlan()
        }

        // if user wants to edit a certain plan
        editingPlanId = intent.getStringExtra("PLAN_ID")
        if(editingPlanId != null) {
            loadExistingPlan(editingPlanId!!)
        }


        setupBottomNavigation()
    }

    // Function that adds new expense row container using expense_row.xml
    private fun addNewExpenseRow(): android.view.View {
        // Creates LayoutInflater to inflate expense_row.xml into the container in plan.xml
        val inflater = LayoutInflater.from(this)
        val rowView = inflater.inflate(R.layout.expense_row, null)

        // Find views in expense row
        val btnDeleteExpense = rowView.findViewById<ImageButton>(R.id.btnDeleteExpense)
        val etExpenseDate = rowView.findViewById<EditText>(R.id.expenseDate)
        val etAmount = rowView.findViewById<EditText>(R.id.expenseAmount)

        etAmount.doAfterTextChanged {
            calculateTotals()
        }

        etExpenseDate.setOnClickListener {
            showDatePicker(etExpenseDate)
        }

        btnDeleteExpense.setOnClickListener {
            expensesContainer.removeView(rowView)

            calculateTotals() // Update math when removed
        }

        // Adds row to activity_plan.xml
        expensesContainer.addView(rowView)
        return rowView
    }

    private fun calculateTotals() {
        // math logic can be implemented here
        val budget = totalBudget.text.toString().toDoubleOrNull() ?: 0.0
        var totalExp = 0.0

        // loop through rows
        for(i in 0 until expensesContainer.childCount) {
            val row = expensesContainer.getChildAt(i)
            val etAmount = row.findViewById<EditText>(R.id.expenseAmount)
            val amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0
            totalExp += amount
        }

        val remaining = budget - totalExp

        // update fields in Budget Overview
        totalExpenses.setText(String.format("%.2f", totalExp))
        remainingBudget.setText(String.format("%.2f", remaining))
    }

    // actual function to save the plan
    private fun savePlan() {
        val name = findViewById<EditText>(R.id.planName).text.toString()
        val desc = findViewById<EditText>(R.id.planDescription).text.toString()
        val dest = findViewById<EditText>(R.id.planDestination).text.toString()
        val start = findViewById<EditText>(R.id.planStartDate).text.toString()
        val end = findViewById<EditText>(R.id.planEndDate).text.toString()
        val budget = totalBudget.text.toString().toDoubleOrNull() ?: 0.0

        // expense rows
        val expenseList = mutableListOf<Expense>()
        for (i in 0 until expensesContainer.childCount) {
            val row = expensesContainer.getChildAt(i)

            // row.findViewById to collect row data
            val expDesc = row.findViewById<EditText>(R.id.expenseDescription).text.toString()
            val expAmount = row.findViewById<EditText>(R.id.expenseAmount).text.toString().toDoubleOrNull() ?: 0.0
            val expDate = row.findViewById<EditText>(R.id.expenseDate).text.toString()

            expenseList.add(Expense(expDesc, expAmount, expDate))
        }

        // create Plan object
        val newPlan = Plan(
            id = editingPlanId?: System.currentTimeMillis().toString(),
            name = name,
            destination = dest,
            description = desc,
            startDate = start,
            endDate = end,
            totalBudget = budget,
            expenses = expenseList
        )

        // presenter save (checks if is in edit mode)
        planPresenter.handleSavePlan(newPlan, editingPlanId != null)

    }


    // direct access from model for id (idk any of this)
    private fun loadExistingPlan(id: String) {
        val plan = planModel.getPlanById(id)
        if(plan != null) {
            findViewById<EditText>(R.id.planName).setText(plan.name)
            findViewById<EditText>(R.id.planDestination).setText(plan.destination)
            findViewById<EditText>(R.id.planDescription).setText(plan.description)
            findViewById<EditText>(R.id.planStartDate).setText(plan.startDate)
            findViewById<EditText>(R.id.planEndDate).setText(plan.endDate)
            totalBudget.setText(plan.totalBudget.toString())

            // fill expense rows
            plan.expenses.forEach { expense ->
                val rowView = addNewExpenseRow()
                rowView.findViewById<EditText>(R.id.expenseDescription).setText(expense.description)
                rowView.findViewById<EditText>(R.id.expenseAmount).setText(expense.amount.toString())
                rowView.findViewById<EditText>(R.id.expenseDate).setText(expense.date)
            }
            calculateTotals()
        }
    }


    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
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

    override fun onSaveSuccess() {
        toast("Plan saved successfully!")
        finish()
    }

    override fun onError(message: String) {
        toast(message)
    }
}
