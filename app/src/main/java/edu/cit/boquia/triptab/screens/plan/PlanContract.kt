package edu.cit.boquia.triptab.screens.plan

interface PlanContract {
    interface View {
        fun onSaveSuccess()
        fun onError(message: String)
    }

    interface Presenter {
        fun handleSavePlan(plan: Plan, isEdited: Boolean)
    }
}
