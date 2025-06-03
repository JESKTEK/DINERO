package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Goals : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goals)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        val navBar = findViewById<BottomNavigationView>(R.id.bottomNav)
        navBar.selectedItemId = R.id.goals
        navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.budget -> {
                    val intent = Intent(this, BudgetHomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.goals -> {
                    true
                }
                R.id.dashboard -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> {false}
            }
        }



    }
}