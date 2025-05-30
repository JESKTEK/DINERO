package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBudgetPlan = findViewById<Button>(R.id.btnBudgetPlan)
        val btnMyGoals = findViewById<Button>(R.id.btnMyGoals)
        val btnDashboard = findViewById<Button>(R.id.btnDashboard)
        val plusSign = findViewById<TextView>(R.id.plusSign)

        // Navigate to Budget Page
        btnBudgetPlan.setOnClickListener {
            val intent = Intent(this, BudgetHomePageActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Dashboard Page
        btnDashboard.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        // Coming soon for Goals
        btnMyGoals.setOnClickListener {
            Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show()
        }

        // Coming soon for +
        plusSign.setOnClickListener {
            Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }
}
