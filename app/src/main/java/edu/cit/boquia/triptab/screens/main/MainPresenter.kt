package edu.cit.boquia.triptab.screens.main

import edu.cit.boquia.triptab.screens.plan.PlanModel

class MainPresenter(
    private val view: MainContract.View,
    private val model: PlanModel
) : MainContract.Presenter {

    override fun loadDashboard() {
        val plans = model.getAllPlans()
        if (plans.isEmpty()) {
            view.showEmptyState()
        } else {
            view.displayPlans(plans)
        }
    }

    override fun deletePlan(id: String) {
        model.deletePlan(id)
    }
}
