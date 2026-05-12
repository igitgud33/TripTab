package edu.cit.boquia.triptab.utils

import android.app.DatePickerDialog
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.cit.boquia.triptab.R
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
