package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var loginTransitButton: AppCompatButton
    private lateinit var registerTransitButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginTransitButton = findViewById(R.id.loginButton)
        registerTransitButton = findViewById(R.id.registerButton)

        loginTransitButton.setOnClickListener {
            val ic = Intent(this, LoginActivity::class.java)
            startActivity(ic)
        }

        registerTransitButton.setOnClickListener {
            val ix = Intent(this, RegisterActivity::class.java)
            startActivity(ix)
        }

    }


}