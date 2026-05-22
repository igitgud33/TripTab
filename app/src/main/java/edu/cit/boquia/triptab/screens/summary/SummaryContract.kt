package edu.cit.boquia.triptab.screens.summary

import edu.cit.boquia.triptab.screens.plan.Plan

interface SummaryContract {
    interface View {
        fun displaySummary(plans: List<Plan>, totalExpenses: Double)
        fun showEmptyState()
    }

    interface Presenter {
        fun loadSummary()
    }
}