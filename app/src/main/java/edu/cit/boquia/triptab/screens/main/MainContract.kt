package edu.cit.boquia.triptab.screens.main

import edu.cit.boquia.triptab.screens.plan.Plan

interface MainContract {
    interface View {
        fun displayPlans(plans: List<Plan>)
        fun showEmptyState()
        fun toCreatePlan()

    }

    interface Presenter {
        fun loadDashboard() // gets plans from Model, otherwise showEmptyState
        fun deletePlan(id: String)
    }
}