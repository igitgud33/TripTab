package edu.cit.boquia.triptab.screens.plan

class PlanPresenter(
    private val view: PlanContract.View,
    private val model: PlanModel,
) : PlanContract.Presenter {

    // isEditing to check if Plan is being updated / edited
    override fun handleSavePlan(plan: Plan, isEditing: Boolean) {
        if (plan.name.isEmpty()) {
            view.onError("Please enter a plan name")
            return
        }
        if(isEditing) {
            model.updatePlan(plan)
        } else {
            model.savePlan(plan)
        }


        view.onSaveSuccess()
    }
}
