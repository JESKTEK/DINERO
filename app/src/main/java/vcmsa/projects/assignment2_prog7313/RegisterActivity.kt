package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val firestore = Firebase.firestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var loginTransitButton: Button
    private lateinit var registerButton: Button
    private lateinit var passwordText: EditText
    private lateinit var passwordConfirmText: EditText
    private lateinit var emailText: EditText
    private lateinit var fullNameText: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        //userDao = AppDatabase.getInstance(this).userDao()

        passwordText = findViewById(R.id.editTextPassword)
        emailText = findViewById(R.id.editTextEmailAddress)
        passwordConfirmText = findViewById(R.id.editTextPasswordConfirm)
        registerButton = findViewById(R.id.registerButton)
        fullNameText = findViewById(R.id.editTextFullName)
        loginTransitButton = findViewById(R.id.loginTransitButton)

        registerButton.setOnClickListener {
            registerUser()
        }

        loginTransitButton.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }

    private fun registerUser() {
        val email = emailText.text.toString().trim()
        val password = passwordText.text.toString().trim()
        val confirmText = passwordConfirmText.text.toString().trim()
        val fullName = fullNameText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Email, Password, and Name must not be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        if (confirmText != password) {
            Toast.makeText(this, "Password and Confirm Password must match.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val user = User(name = fullName, email = email)
                            //userDao.insertUser(user)
                            firestore.collection("Users").add(user)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "User $fullName created successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Log.e("RegisterActivity", "User creation failed", e)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "User creation failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Log.e("RegisterActivity", "Firebase Authentication failed", task.exception)
                    Toast.makeText(
                        this,
                        "Sorry, there was an error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}