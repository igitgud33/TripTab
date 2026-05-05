package edu.cit.boquia.triptab.screens.register

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.data.User
import edu.cit.boquia.triptab.screens.login.LoginActivity
import edu.cit.boquia.triptab.utils.getEditTextValue
import edu.cit.boquia.triptab.utils.toast
import java.util.Calendar

class RegisterActivity : AppCompatActivity(), RegisterContract.View {
    private lateinit var registerPresenter: RegisterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)


        // init presenter
        registerPresenter = RegisterPresenter(this, RegisterModel(application as CustomApp))

        // Detecting register info
        val etBirthDate = findViewById<EditText>(R.id.registerBirthDate)

        etBirthDate.setOnClickListener {
            showDatePicker(etBirthDate)

        }

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            // save data to User object
            val newUser = User(
                name = getEditTextValue(R.id.registerFullName),
                email = getEditTextValue(R.id.registerEmail),
                birthDate = getEditTextValue(R.id.registerBirthDate),
                phoneNo = getEditTextValue(R.id.registerPhoneNumber),
                password = getEditTextValue(R.id.registerPassword)

            )
            // hand to presenter
            registerPresenter.registerUser(newUser)

        }
        val btnRegisterBack = findViewById<ImageButton>(R.id.btnRegisterBack)
        btnRegisterBack.setOnClickListener {
            finish()
        }

    }




    override fun onSuccess() {
        toast("Registration Successful!")
    }

    override fun onEmpty() {
        toast("All fields must not be empty.")
    }

    override fun onInvalid() {
        toast("Invalid credentials. Please try again.")
    }

    override fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    // calendar display logic
    override fun showDatePicker(etBirthDate: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // format mm/dd/yyyy
            val formattedDate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
            etBirthDate.setText(formattedDate)
        }, year, month, day)

        datePicker.show()
    }

}
