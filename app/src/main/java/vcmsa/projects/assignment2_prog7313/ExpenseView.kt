package vcmsa.projects.assignment2_prog7313

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import vcmsa.projects.assignment2_prog7313.databinding.ActivityExpenseViewBinding

class ExpenseView : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseViewBinding
    private lateinit var expenseAdapter: ExpenseAdapter
    private val firestore = Firebase.firestore

    private var monthlyBudgetMin: Double = 0.0
    private var monthlyBudgetMax: Double = 100000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        monthlyBudgetMin = prefs.getFloat("monthlyBudgetMin", 0f).toDouble()
        monthlyBudgetMax = prefs.getFloat("monthlyBudgetMax", 100000f).toDouble()

        expenseAdapter = ExpenseAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = expenseAdapter

        lifecycleScope.launch {
            loadExpenses()
        }

        binding.btnSetBudget.setOnClickListener {
            showSetBudgetDialog()
        }
    }

    private suspend fun loadExpenses() {
        firestore.collection("Expenses").get()
            .addOnSuccessListener { snapshot ->
                val expenses = snapshot.documents.map { doc ->
                    Expense(
                        id = doc.getString("id") ?: "",
                        parentId = doc.getString("parentId") ?: "",
                        categoryName = doc.getString("categoryName") ?: "",
                        itemName = doc.getString("itemName") ?: "",
                        amountSpent = doc.getLong("amountSpent")?.toDouble() ?: 0.0,
                        dateCreated = doc.getString("dateCreated") ?: "",
                        uploadImage = doc.getString("uploadImage") ?: "",
                        emailAssociated = doc.getString("emailAssociated") ?: "",
                        details = doc.getString("details") ?: "",
                    )
                }

                expenseAdapter.updateData(expenses)

                val totalSpent = expenses.sumOf { it.amountSpent }
                val percentUsed = if (monthlyBudgetMax > 0)
                    ((totalSpent / monthlyBudgetMax) * 100).toInt().coerceAtMost(100)
                else 0

                binding.budgetProgressBar.progress = percentUsed
                binding.amountSpentText.text = "R${"%.2f".format(totalSpent)} spent"
                binding.budgetText.text = "Monthly Budget: R${"%.2f".format(monthlyBudgetMin)} - R${"%.2f".format(monthlyBudgetMax)}"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching data from Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showSetBudgetDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 10)
        }

        val inputMin = EditText(this).apply {
            hint = "Minimum budget (e.g. 2000)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        val inputMax = EditText(this).apply {
            hint = "Maximum budget (e.g. 100000)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        layout.addView(inputMin)
        layout.addView(inputMax)

        AlertDialog.Builder(this)
            .setTitle("Set Monthly Budget Range")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val min = inputMin.text.toString().toDoubleOrNull()
                val max = inputMax.text.toString().toDoubleOrNull()

                if (min != null && max != null && min < max) {
                    monthlyBudgetMin = min
                    monthlyBudgetMax = max

                    getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
                        .putFloat("monthlyBudgetMin", min.toFloat())
                        .putFloat("monthlyBudgetMax", max.toFloat())
                        .apply()

                    lifecycleScope.launch {
                        loadExpenses()
                    }
                } else {
                    Toast.makeText(this, "Please enter a valid min < max", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
