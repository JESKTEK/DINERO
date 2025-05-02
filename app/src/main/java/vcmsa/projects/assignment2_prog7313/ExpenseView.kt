package vcmsa.projects.assignment2_prog7313

import android.R
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.ParseException
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import vcmsa.projects.assignment2_prog7313.databinding.ActivityExpenseViewBinding
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import kotlin.io.encoding.Base64
import java.util.Locale

class ExpenseView : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseViewBinding
    private lateinit var expenseAdapter: ExpenseAdapter
    private val firestore = Firebase.firestore


    private var unfilteredExpenses: List<Expense> = emptyList()
    private var filteredExpenses: List<Expense> = emptyList()

    private var monthlyBudgetMin: Double = 0.0
    private var monthlyBudgetMax: Double = 100000.0

    private var selectedCategory: String? = null
    private var fromDateS: String? = null
    private var toDateS: String? = null

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

    }


    private fun filterExpenses() {
        filteredExpenses = unfilteredExpenses.filter { expense ->
            val categoryMatch = selectedCategory == null || expense.categoryName == selectedCategory || selectedCategory == "All Categories"
            val dateMatch = (fromDateS == null || toDateS == null) || filterExpensesDate(expense)

            categoryMatch && dateMatch
        }
        expenseAdapter.updateData(filteredExpenses)
    }


    private fun filterExpensesDate(expense: Expense): Boolean {
        val fromDate = convertStringDate(fromDateS!!)
        val toDate = convertStringDate(toDateS!!)
        val expenseDate: Date = convertStringDate(expense.dateCreated)
        return expenseDate.after(fromDate) && expenseDate.before(toDate)
    }

    private fun convertStringDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        val fromDate: Date = try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid From Date format", Toast.LENGTH_SHORT).show()
            return Date()
        }
        return fromDate
    }


    private val addExpenseResultLaunch = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { result ->
        if (result.resultCode == RESULT_OK) {

            val id = result.data?.getStringExtra("id") ?: return@registerForActivityResult
            val parentId =
                result.data?.getStringExtra("parentId") ?: return@registerForActivityResult
            val categoryName =
                result.data?.getStringExtra("categoryName") ?: return@registerForActivityResult
            val itemName =
                result.data?.getStringExtra("itemName") ?: return@registerForActivityResult
            val amountSpent =
                result.data?.getDoubleExtra("amountSpent", 0.0) ?: return@registerForActivityResult
            val dateCreated =
                result.data?.getStringExtra("dateCreated") ?: return@registerForActivityResult
            val uploadImage =
                result.data?.getStringExtra("uploadImage") ?: return@registerForActivityResult
            val details = result.data?.getStringExtra("details") ?: return@registerForActivityResult
            val emailAssociated =
                result.data?.getStringExtra("emailAssociated") ?: return@registerForActivityResult

            lifecycleScope.launch {
                val expense = Expense(
                    id = id,
                    parentId = parentId,
                    categoryName = categoryName,
                    itemName = itemName,
                    amountSpent = amountSpent,
                    dateCreated = dateCreated,
                    uploadImage = uploadImage,
                    details = details,
                    emailAssociated = emailAssociated
                )
                //database.postDao().insertPost(newPost)
                loadExpenses()
            }
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

                unfilteredExpenses = expenses
                expenseAdapter.updateData(expenses)
                updateCatSpinner()

                val totalSpent = expenses.sumOf { it.amountSpent }
                val percentUsed = if (monthlyBudgetMax > 0)
                    ((totalSpent / monthlyBudgetMax) * 100).toInt().coerceAtMost(100)
                else 0
                /******
                • Title: ProgressBar | Android Developers
                • Author: Android Developers
                • Date: 2019
                • Code version: Not specified
                • Availability: https://developer.android.com/reference/android/widget/ProgressBar
                • Accessed: 1 May 2025
                *****/
                binding.budgetProgressBar.progress = percentUsed
                binding.amountSpentText.text = "R${"%.2f".format(totalSpent)} spent"
                binding.budgetText.text = "Monthly Budget: R${"%.2f".format(monthlyBudgetMin)} - R${"%.2f".format(monthlyBudgetMax)}"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching data from Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateCatSpinner() {
        val uniqueCategories = unfilteredExpenses.map { it.categoryName }.distinct().toMutableList()
        uniqueCategories.add(0, "All Categories")

        val categorySpinner: Spinner = binding.categorySpinner
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, uniqueCategories)
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

