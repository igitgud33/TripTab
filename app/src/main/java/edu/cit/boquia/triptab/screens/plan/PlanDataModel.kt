package edu.cit.boquia.triptab.screens.plan

data class Plan(
    val id: String,
    val name: String,
    val destination: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val totalBudget: Double,
    val expenses: List<Expense>
)

data class Expense(
    val description: String,
    val amount: Double,
    val date: String
)
