package edu.cit.boquia.triptab

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Detecting register info
        val etFullName = findViewById<EditText>(R.id.registerFullName)
        val etEmail = findViewById<EditText>(R.id.registerEmail)
        val etBirthDate = findViewById<EditText>(R.id.registerBirthDate)
        val etPhoneNo = findViewById<EditText>(R.id.registerPhoneNumber)
        val etPassword = findViewById<EditText>(R.id.registerPassword)


        etBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, {_, selectedYear, selectedMonth, selectedDay ->
                // format mm/dd/yyyy
                val formattedDate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
                etBirthDate.setText(formattedDate)
            }, year, month, day)

            datePicker.show()


        }

        btnRegister.setOnClickListener {
            // Converting to string
            val name = etFullName.text.toString()
            val email = etEmail.text.toString()
            val birthDate = etBirthDate.text.toString()
            val phoneNo = etPhoneNo.text.toString()
            val password = etPassword.text.toString()

            //SharedPreferences
            val sharedPref = getSharedPreferences("TripTabPrefs", MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.putString("NAME", name)
            editor.putString("EMAIL", email)
            editor.putString("BIRTHDATE", birthDate)
            editor.putString("PHONE", phoneNo)
            editor.putString("PASSWORD", password)

            editor.apply()


            // Explicit Intent
            val intent = Intent(this, LoginActivity::class.java)

            // Passing register data
            intent.putExtra("name", name)
            intent.putExtra("email", email)
            intent.putExtra("birthDate", birthDate)
            intent.putExtra("phoneNo", phoneNo)
            intent.putExtra("password", password)

            startActivity(intent)
        }

        val btnRegisterBack = findViewById<ImageButton>(R.id.btnRegisterBack)
        btnRegisterBack.setOnClickListener {
            finish()
        }

    }
}