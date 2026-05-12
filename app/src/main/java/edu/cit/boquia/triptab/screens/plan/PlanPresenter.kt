package edu.cit.boquia.triptab.screens.plan

class PlanPresenter(
    private val view: PlanContract.View,
    private val model: PlanModel,
) : PlanContract.Presenter {

    override fun handleSavePlan(plan: Plan) {
        if (plan.name.isEmpty()) {
            view.onError("Please enter a plan name")
            return
        }

        model.savePlan(plan)
        view.onSaveSuccess()
    }
}
