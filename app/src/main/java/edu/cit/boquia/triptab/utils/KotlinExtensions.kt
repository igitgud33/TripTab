package edu.cit.boquia.triptab.utils

import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.cit.boquia.triptab.R

fun AppCompatActivity.getEditTextValue(id: Int): String {
    return findViewById<EditText>(id).text.toString()
}

fun AppCompatActivity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}