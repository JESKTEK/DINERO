package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var registerTransitButton: Button
    private lateinit var passwordText: EditText
    private lateinit var emailText: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        passwordText = findViewById(R.id.editTextPassword)
        emailText = findViewById(R.id.editTextEmailAddress)
        loginButton = findViewById(R.id.loginButton)
        registerTransitButton = findViewById(R.id.registerTransitButton)

        loginButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and Password must not be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, HomeActivity::class.java)
                    startActivity(i)
                    finish() // <<< THIS was missing!
                } else {
                    Toast.makeText(this, "Login Failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerTransitButton.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java) // <<< It must go to RegisterActivity
            startActivity(i)
        }
    }
}
