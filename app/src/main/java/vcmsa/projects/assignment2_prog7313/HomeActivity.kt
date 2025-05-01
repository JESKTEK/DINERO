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
        btnBudgetPlan.setOnClickListener {
            val intent = Intent(this, BudgetHomePageActivity::class.java)
            startActivity(intent)
        }

        val plusSign = findViewById<TextView>(R.id.plusSign)
        plusSign.setOnClickListener {
            Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show()

            val btnMyGoals = findViewById<TextView>(R.id.btnMyGoals)
            btnMyGoals.setOnClickListener {
                Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show()

                val btnDashboard = findViewById<TextView>(R.id.btnDashboard)
                btnDashboard.setOnClickListener {
                    Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
