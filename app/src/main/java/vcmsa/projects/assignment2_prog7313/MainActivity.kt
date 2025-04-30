package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    private lateinit var loginTransitButton: AppCompatButton
    private lateinit var registerTransitButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        // Optionally delay the splash screen for 2 seconds
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        Handler(Looper.getMainLooper()).postDelayed({
            keepSplashScreen = false
        }, 2000) // 2000ms = 2 seconds delay

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
            val ix = Intent(this, MainActivity::class.java)
            startActivity(ix)
        }

    }


}