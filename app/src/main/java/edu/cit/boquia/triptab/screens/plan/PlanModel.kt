package edu.cit.boquia.triptab.screens.plan

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.cit.boquia.triptab.app.CustomApp
import androidx.core.content.edit

class PlanModel(app: CustomApp) {
    private val sharedPref = app.getSharedPreferences("TripTabPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun savePlan(newPlan: Plan) {
        val allPlans = getAllPlans().toMutableList()
        allPlans.add(newPlan)

        val json = gson.toJson(allPlans)
        sharedPref.edit().putString("SAVED_PLANS", json).apply()
    }

    fun getAllPlans(): List<Plan> {
        val json = sharedPref.getString("SAVED_PLANS", null) ?: return emptyList()
        val type = object : TypeToken<List<Plan>>() {}.type
        return gson.fromJson(json, type)
    }

    fun deletePlan(planId: String) {
        val allPlans = getAllPlans().toMutableList()
        allPlans.removeAll {it.id == planId}

        val json = gson.toJson(allPlans)
        sharedPref.edit().putString("SAVED_PLANS", json).apply()
    }

    fun updatePlan(updatedPlan: Plan) {
        val allPlans = getAllPlans().toMutableList()
        val index = allPlans.indexOfFirst {it.id == updatedPlan.id}

        if(index != -1) {
            allPlans[index] = updatedPlan // replaces selected plan
            val json = gson.toJson(allPlans)
            sharedPref.edit().putString("SAVED_PLANS", json).apply()
        }
    }

    fun getPlanById(id: String): Plan? {
        return getAllPlans().find {it.id == id}
    }

    // plan groups
    fun getAllGroups(): List<PlanGroup> {
        val json = sharedPref.getString("SAVED_GROUPS", null) ?: return emptyList()
        val type = object: TypeToken<List<PlanGroup>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveGroup(group: PlanGroup) {
        val allGroups = getAllGroups().toMutableList()
        allGroups.add(group)
        sharedPref.edit { putString("SAVED_GROUPS", gson.toJson(allGroups)) }
    }

    fun deleteGroup(groupId: String) {
        val allGroups = getAllGroups().toMutableList()
        allGroups.removeAll {it.id == groupId}

        val json = gson.toJson(allGroups)
        sharedPref.edit().putString("SAVED_GROUPS", json).apply()
    }
}
