package vcmsa.projects.assignment2_prog7313

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import vcmsa.projects.assignment2_prog7313.databinding.ActivityExpenseViewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseView : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseViewBinding
    private lateinit var expenseAdapter: ExpenseAdapter
    private val firestore = Firebase.firestore

    private var unfilteredExpenses: List<Expense> = emptyList()
    private var filteredExpenses: List<Expense> = emptyList()

    private var monthlyBudgetMin: Double = 0.0
    private var monthlyBudgetMax: Double = 100000.0

    private lateinit var auth: FirebaseAuth
    private lateinit var ivDineroLogo: ImageView
    private var selectedCategory: String? = null
    private var fromDateS: String? = null
    private var toDateS: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        monthlyBudgetMin = prefs.getFloat("monthlyBudgetMin", 0f).toDouble()
        monthlyBudgetMax = prefs.getFloat("monthlyBudgetMax", 100000f).toDouble()

        expenseAdapter = ExpenseAdapter(emptyList())

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = expenseAdapter

        lifecycleScope.launch {
            loadExpenses()
        }

        ivDineroLogo = findViewById(vcmsa.projects.assignment2_prog7313.R.id.ivDineroLogo)
        ivDineroLogo.setOnClickListener { view ->
            showLogoutPopupMenu(view)
        }

        binding.btnSetBudget.setOnClickListener {
            Toast.makeText(this, "Set Budget", Toast.LENGTH_SHORT).show()
            showSetBudgetDialog()
        }

        /*****
        Title: How to Implement DatePickerDialog in Android Using Kotlin
        Author: Abhishek Suman
        Date: 22 April 2025
        Availability: https://medium.com/%40abhisheksuman413/how-to-implement-datepickerdialog-in-android-using-kotlin-45c413e47464
         *****/
        var datesInput1 = false
        var datesInput2 = false
        binding.fromDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.fromDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                if (datesInput1 && datesInput2) {
                    fromDateS = binding.fromDate.text.toString()
                    toDateS = binding.toDate.text.toString()
                    filterExpenses()
                }
            }, year, month, day)

            datePicker.show()
            datesInput1 = true
        }

        binding.toDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.toDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                datesInput2 = true
                if (datesInput1 && datesInput2) {
                    fromDateS = binding.fromDate.text.toString()
                    toDateS = binding.toDate.text.toString()
                    filterExpenses()
                }
            }, year, month, day)

            datePicker.show()
        }

        val categorySpinner: Spinner = binding.categorySpinner
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedCategory = parent.getItemAtPosition(position).toString()
                filterExpenses()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedCategory = null
                filterExpenses()
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, BudgetHomePageActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Bottom Navigation Bar setup
        binding.bottomNav.selectedItemId = vcmsa.projects.assignment2_prog7313.R.id.home
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                vcmsa.projects.assignment2_prog7313.R.id.home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                vcmsa.projects.assignment2_prog7313.R.id.goals -> {
                    val intent = Intent(this, Goals::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                vcmsa.projects.assignment2_prog7313.R.id.dashboard -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> {false}
            }
        }
    }


    private fun filterExpenses() {
        val expList = filterExpensesAccount()
        filteredExpenses = expList.filter { expense ->
            val categoryMatch = selectedCategory == null || expense.categoryName == selectedCategory || selectedCategory == "All Categories"
            val dateMatch = (fromDateS == null || toDateS == null) || filterExpensesDate(expense)

            categoryMatch && dateMatch
        }
        expenseAdapter.updateData(filteredExpenses)
    }


    private fun filterExpensesAccount() : List<Expense> {
        val userEmail = auth.currentUser?.email.toString()
        val exp = unfilteredExpenses.filter { category ->
            userEmail.equals(category.emailAssociated)
        }
        return exp
    }


    private fun filterExpensesDate(expense: Expense): Boolean {
        // Ensure fromDateS and toDateS are not null before parsing
        if (fromDateS == null || toDateS == null) return false

        val fromDate = convertStringDate(fromDateS!!)
        val toDate = convertStringDate(toDateS!!)
        val expenseDate: Date = convertStringDate(expense.dateCreated)

        // Check if expenseDate is within the range [fromDate, toDate] inclusive
        return !expenseDate.before(fromDate) && !expenseDate.after(toDate)
    }

    private fun convertStringDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date = try {
            dateFormat.parse(dateString) ?: Date() // Provide a default if parsing fails
        } catch (e: Exception) {
            Log.e("ExpenseView", "Error parsing date: $dateString", e)
            Toast.makeText(this, "Invalid Date format encountered.", Toast.LENGTH_SHORT).show()
            Date() // Return current date on error
        }
        return date
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
                        amountSpent = doc.getDouble("amountSpent") ?: 0.0, // Use getDouble directly
                        dateCreated = doc.getString("dateCreated") ?: "",
                        uploadImage = doc.getString("uploadImage") ?: "",
                        emailAssociated = doc.getString("emailAssociated") ?: "",
                        details = doc.getString("details") ?: "",
                    )
                }

                unfilteredExpenses = expenses
                filterExpenses() // Apply filters after loading all expenses
                updateCatSpinner()

                val currentUsersExpenses = filterExpensesAccount()
                val totalSpent = currentUsersExpenses.sumOf { it.amountSpent }
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

    private fun updateCatSpinner() {
        val uniqueCategories = unfilteredExpenses
            .filter { it.emailAssociated == auth.currentUser?.email } // Filter categories by current user
            .map { it.categoryName }
            .distinct()
            .toMutableList()
        uniqueCategories.add(0, "All Categories")

        val categorySpinner: Spinner = binding.categorySpinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, uniqueCategories) // Use android.R.layout for default spinner item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun showSetBudgetDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 10)
        }

        val inputMin = EditText(this).apply {
            hint = "Minimum budget (e.g. 2000)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(monthlyBudgetMin.toString()) // Pre-fill with current min budget
        }

        val inputMax = EditText(this).apply {
            hint = "Maximum budget (e.g. 100000)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(monthlyBudgetMax.toString()) // Pre-fill with current max budget
        }

        layout.addView(inputMin)
        layout.addView(inputMax)

        AlertDialog.Builder(this)
            .setTitle("Set Monthly Budget Range")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val min = inputMin.text.toString().toDoubleOrNull()
                val max = inputMax.text.toString().toDoubleOrNull()

                if (min != null && max != null && min <= max) { // Changed to min <= max
                    monthlyBudgetMin = min
                    monthlyBudgetMax = max

                    getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
                        .putFloat("monthlyBudgetMin", min.toFloat())
                        .putFloat("monthlyBudgetMax", max.toFloat())
                        .apply()

                    lifecycleScope.launch {
                        loadExpenses() // Reload expenses and update UI
                    }
                } else {
                    Toast.makeText(this, "Please enter valid numbers where Minimum <= Maximum", Toast.LENGTH_SHORT).show()
                }

            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Show the logout popup menu
    private fun showLogoutPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.logout_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    // Sign out the user
                    auth.signOut()
                    Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()

                    // Redirect to LoginActivity and clear activity stack
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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