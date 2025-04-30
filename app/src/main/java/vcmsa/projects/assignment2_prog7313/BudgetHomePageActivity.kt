package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.cardview.widget.CardView


class BudgetHomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_home_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val createCategoryCard = findViewById<CardView>(R.id.cardCreateCategory)
        createCategoryCard.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        val viewBudgetCard = findViewById<CardView>(R.id.cardViewBudget)
        viewBudgetCard.setOnClickListener {
            val intent = Intent(this, CategoryView::class.java)
            startActivity(intent)
        }
    }
}
