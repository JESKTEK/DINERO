package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BudgetHomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_home_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
    }
}
