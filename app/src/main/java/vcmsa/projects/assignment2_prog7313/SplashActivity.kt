package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)  // Set the splash screen layout

        // Handler to delay transition to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Finish SplashActivity so it doesn't remain in the stack
        }, 3000)  // Delay in milliseconds (2 seconds)
    }
}
