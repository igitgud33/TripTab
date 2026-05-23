package edu.cit.boquia.triptab.screens.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.data.User
import edu.cit.boquia.triptab.screens.summary.SummaryActivity
import edu.cit.boquia.triptab.screens.login.LoginActivity
import edu.cit.boquia.triptab.screens.main.MainActivity
import edu.cit.boquia.triptab.utils.setupBottomNavigation
import edu.cit.boquia.triptab.utils.toast

class ProfileActivity : AppCompatActivity(), ProfileContract.View {
    private lateinit var profilePresenter: ProfilePresenter
    private lateinit var selectedImageUri: Uri
    private var currentUserPassword = ""

    private var currentImageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // init presenter
        profilePresenter = ProfilePresenter(this, ProfileModel(application as CustomApp))

        // to display data from MainActivity
        profilePresenter.loadUserData()

        val btnProfileLogOut = findViewById<Button>(R.id.btnProfileLogOut)
        btnProfileLogOut.setOnClickListener {
            // clears profile info from shared preferences

            profilePresenter.logOut()

        }

        // pfp block

        // image picker
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it

                // allows persisting permission to use pfp
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)

                findViewById<ImageView>(R.id.ivProfilePic).setImageURI(it)
            }

        }

        findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
            pickImage.launch("image/*")
        }

        // save changes
        findViewById<Button>(R.id.btnSaveProfile).setOnClickListener {
            val updatedUser = User(
                name = findViewById<EditText>(R.id.etProfileName).text.toString(),
                email = findViewById<EditText>(R.id.etEmail).text.toString(),
                birthDate = findViewById<EditText>(R.id.etBirthDate).text.toString(),
                phoneNo = findViewById<EditText>(R.id.etPhoneNumber).text.toString(),
                password = currentUserPassword,
                profileImageUri = if (::selectedImageUri.isInitialized) selectedImageUri.toString() else currentImageUri
            )

            profilePresenter.updateProfile(updatedUser)
            toast("Profile updated successfully!")
        }

        setupBottomNavigation(R.id.bottom_nav_profile, R.id.nav_profile)

    }

    override fun displayUserData(user: User) {
        val etName = findViewById<EditText>(R.id.etProfileName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etBirthDate = findViewById<EditText>(R.id.etBirthDate)
        val etPhoneNo = findViewById<EditText>(R.id.etPhoneNumber)
        currentUserPassword = user.password
        currentImageUri = user.profileImageUri // saves current pfp

        etName.setText(user.name)
        etEmail.setText(user.email)
        etBirthDate.setText(user.birthDate)
        etPhoneNo.setText(user.phoneNo)

        user.profileImageUri?.let {
            findViewById<ImageView>(R.id.ivProfilePic).setImageURI(Uri.parse(it))
        }
    }

    override fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)

        // clear back stack so user stays in Login
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }

}