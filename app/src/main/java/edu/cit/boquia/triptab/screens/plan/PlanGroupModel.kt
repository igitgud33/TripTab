package edu.cit.boquia.triptab.screens.plan

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.cit.boquia.triptab.app.CustomApp

class PlanGroupModel(app: CustomApp) {
    private val sharedPref = app.getSharedPreferences("TripTabPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

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