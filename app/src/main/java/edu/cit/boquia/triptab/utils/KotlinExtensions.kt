package edu.cit.boquia.triptab.utils

import android.app.DatePickerDialog
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.screens.main.MainActivity
import edu.cit.boquia.triptab.screens.profile.ProfileActivity
import edu.cit.boquia.triptab.screens.summary.SummaryActivity
import java.util.Calendar

fun AppCompatActivity.getEditTextValue(id: Int): String {
    return findViewById<EditText>(id).text.toString()
}

fun AppCompatActivity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}

fun AppCompatActivity.showDatePicker(etCalendarField: EditText) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
        // format mm/dd/yyyy
        val formattedDate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
        etCalendarField.setText(formattedDate)
    }, year, month, day).show()
}

// confirm dialog
fun AppCompatActivity.showConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit
) {
    android.app.AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Yes") {_, _ -> onConfirm()}
        .setNegativeButton("No", null)
        .show()
}

// more convenient way to display payment amounts
val Double.toCurrency: String
    get() = "₱ ${String.format("%.2f", this)}"


fun AppCompatActivity.setupBottomNavigation(navViewId: Int, selectedItemId: Int) {
    val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(navViewId)
    bottomNav.selectedItemId = selectedItemId

    bottomNav.setOnItemSelectedListener { item ->
        // if already on selected screen, do nothing
        if (item.itemId == selectedItemId) return@setOnItemSelectedListener true

        val intent = when(item.itemId) {
            R.id.nav_home -> Intent(this, MainActivity::class.java)
            R.id.nav_summary -> Intent(this, SummaryActivity::class.java)
            R.id.nav_profile -> Intent(this, ProfileActivity::class.java)
            else -> null

        }

        intent?.let {
            startActivity(it)

            // custom transition (deprecated)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            true
        } ?: false
    }
}
