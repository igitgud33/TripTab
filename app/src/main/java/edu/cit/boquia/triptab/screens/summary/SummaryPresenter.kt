package edu.cit.boquia.triptab.screens.summary

import edu.cit.boquia.triptab.screens.plan.PlanModel

class SummaryPresenter(
    private val view: SummaryContract.View,
    private val model: PlanModel
): SummaryContract.Presenter {

    override fun loadSummary() {
        val plans = model.getAllPlans()
        if(plans.isEmpty()) {
            view.showEmptyState()
        } else {
            val total = plans.sumOf { it.expenses.sumOf { e -> e.amount } }
            view.displaySummary(plans, total)
        }

    }
}