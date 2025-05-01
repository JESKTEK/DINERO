package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var loginTransitButton: AppCompatButton
    private lateinit var registerTransitButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        // Delay splash screen for 2 seconds
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        Handler(Looper.getMainLooper()).postDelayed({
            keepSplashScreen = false
        }, 2000)

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
        val fromJesktekText: TextView = findViewById(R.id.fromJesktek)

        /*****
        Title: Android: underlined text in a TextView
        Author: Paolo Montalto
        Date: 20 April 2025
        Availability: https://xabaras.medium.com/android-underlined-text-in-a-textview-ff647d427bab
         *****/
        // Set underlined text
        val underlinedText = SpannableString("FROM JESKTEK")
        underlinedText.setSpan(UnderlineSpan(), 0, underlinedText.length, 0)
        fromJesktekText.text = underlinedText

        // Open GitHub on click
        fromJesktekText.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JESKTEK"))
            startActivity(browserIntent)
        }

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
