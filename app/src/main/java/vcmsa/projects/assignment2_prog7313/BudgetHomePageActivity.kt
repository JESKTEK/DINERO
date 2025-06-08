package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class BudgetHomePageActivity : AppCompatActivity() {

    private lateinit var ivDineroLogo: ImageView

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_home_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivDineroLogo = findViewById(R.id.ivDineroLogo)

        ivDineroLogo.setOnClickListener { view ->
            showLogoutPopupMenu(view)
        }

        // Create Category Button
        val createCategoryBtn = findViewById<LinearLayout>(R.id.btnCreateCategory)
        createCategoryBtn.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        // View Budget Button
        val viewBudgetBtn = findViewById<LinearLayout>(R.id.btnViewBudget)
        viewBudgetBtn.setOnClickListener {
            val intent = Intent(this, CategoryView::class.java)
            startActivity(intent)
        }

        // View Expense Button
        val viewExpensesBtn = findViewById<LinearLayout>(R.id.btnViewExpense)
        viewExpensesBtn.setOnClickListener {
            val intent = Intent(this, ExpenseView::class.java)
            startActivity(intent)
        }
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val navBar = findViewById<BottomNavigationView>(R.id.bottomNav)
        navBar.selectedItemId = R.id.home
        navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.goals -> {
                    val intent = Intent(this, Goals::class.java)
                    startActivity(intent)
                    finish()
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

    // Shows the logout popup menu
    private fun showLogoutPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.logout_menu, popup.menu) // Make sure logout_menu.xml exists in res/menu/

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_chatbot -> {
                    val intent = Intent(this, ChatbotActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.calculator_layout -> {
                    val intent = Intent(this, activity_calculator::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_logout -> {
                    // Sign out the user
                    auth.signOut()
                    Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()

                    // Redirect to LoginActivity and clear activity stack
                    val intent = Intent(this, LoginActivity::class.java) // Assuming LoginActivity is your login screen
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clears back stack
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}