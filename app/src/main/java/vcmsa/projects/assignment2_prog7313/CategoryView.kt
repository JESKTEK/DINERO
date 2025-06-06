package vcmsa.projects.assignment2_prog7313

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import vcmsa.projects.assignment2_prog7313.databinding.ActivityCategoryViewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class CategoryView : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryViewBinding
    private lateinit var categoryAdapter: CategoryAdapter

    private var unfilteredCategories: List<Category> = emptyList()
    private var filteredCategories: List<Category> = emptyList()

    //private lateinit var database: AppDatabase
    private lateinit var auth: FirebaseAuth
    private val firestore = Firebase.firestore

    private lateinit var ivDineroLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding = ActivityCategoryViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //database = AppDatabase.getInstance(this)
        //syncFirebaseToRoom()

        categoryAdapter = CategoryAdapter(emptyList())

        ivDineroLogo = binding.root.findViewById(R.id.ivDineroLogo)
        ivDineroLogo.setOnClickListener { view ->
            showLogoutPopupMenu(view)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = categoryAdapter

        lifecycleScope.launch {
            loadCategories()
        }

        /*****
        Title: How to Implement DatePickerDialog in Android Using Kotlin
        Author: Abhishek Suman
        Date: 22 April 2025
        Availability: https://medium.com/%40abhisheksuman413/how-to-implement-datepickerdialog-in-android-using-kotlin-45c413e47464
         *****/
        var datesInput1 = false
        var datesInput2 = false
        binding.fromDateSel.setOnClickListener() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.fromDateSel.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                var cat = filterCategoriesAccount()
                if (datesInput1 && datesInput2) {
                    cat = filterCategoriesDate(
                        binding.fromDateSel.text.toString(),
                        binding.toDateSel.text.toString(),
                        cat
                    )
                }
                filteredCategories = cat
                categoryAdapter.updateData(cat)
            }, year, month, day)

            datePicker.show()
            datesInput1 = true

        }
        binding.toDateSel.setOnClickListener() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.toDateSel.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                datesInput2 = true
                var cat = filterCategoriesAccount()
                if (datesInput1 && datesInput2) {
                    cat = filterCategoriesDate(
                        binding.fromDateSel.text.toString(),
                        binding.toDateSel.text.toString(),
                        cat
                    )
                }
                filteredCategories = cat
                categoryAdapter.updateData(cat)
            }, year, month, day)

            datePicker.show()

        }

        //enableEdgeToEdge()
        /*setContentView(R.layout.activity_category_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, BudgetHomePageActivity::class.java)
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


    private val addCategoryResultLaunch = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { result ->
        if (result.resultCode == RESULT_OK) {
            val id = result.data?.getStringExtra("id") ?: return@registerForActivityResult
            val catName = result.data?.getStringExtra("catName") ?: return@registerForActivityResult
            val dateCreated =
                result.data?.getStringExtra("dateCreated") ?: return@registerForActivityResult
            val emailAssociated =
                result.data?.getStringExtra("emailAssociated") ?: return@registerForActivityResult
            val description =
                result.data?.getStringExtra("description") ?: return@registerForActivityResult
            val amountSpent =
                result.data?.getDoubleExtra("amountSpent", 0.0) ?: return@registerForActivityResult
            val amountBudgeted = result.data?.getDoubleExtra("amountBudgeted", 0.0)
                ?: return@registerForActivityResult

            lifecycleScope.launch {
                val category = Category(
                    id = id,
                    catName = catName,
                    dateCreated = dateCreated,
                    emailAssociated = emailAssociated,
                    description = description,
                    amountSpent = amountSpent,
                    amountBudgeted = amountBudgeted
                )
                //database.postDao().insertPost(newPost)
                loadCategories()
            }
        }

    }
    private fun filterCategoriesDate(fromDateS: String, toDateS: String, categoryList: List<Category> = unfilteredCategories) : List<Category> {


        val fromDate = convertStringDate(fromDateS)
        val toDate = convertStringDate(toDateS)

        val catList = categoryList.filter { category ->
            val expenseDate : Date = convertStringDate(category.dateCreated)
            // Use logical AND for date range filtering
            expenseDate.after(fromDate) && expenseDate.before(toDate)

        }
        return catList

        //categoryAdapter.updateData(filteredCategories)
    }


    private fun filterCategoriesAccount() : List<Category> {


        val cat = unfilteredCategories.filter { category ->
            val userEmail = auth.currentUser?.email.toString()
            userEmail.equals(category.emailAssociated)

        }
        return cat
        //categoryAdapter.updateData(filteredCategories)
    }


    private fun convertStringDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        val fromDate: Date = try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid From Date format", Toast.LENGTH_SHORT).show()
            Log.e("CategoryView", "Date parsing error: ${e.message}") // Log the error
            return Date() // Return current date as fallback or throw an error
        }
        return fromDate
    }

    private suspend fun loadCategories() {
        firestore.collection("Categories").get()
            .addOnSuccessListener { snapshot ->
                val categories = snapshot.documents.map { doc ->

                    /*val base64Image = doc.getString("Image") // Assuming "Image" field stores base64
                    val bitmap = if (base64Image != null) {
                        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    } else {
                        null // Or a default bitmap if no image is available
                    }*/

                    Category(
                        id = doc.getString("id") ?: "",
                        catName = doc.getString("catName") ?: "",
                        dateCreated = doc.getString("dateCreated") ?: "",
                        emailAssociated = doc.getString("emailAssociated") ?: "",
                        description = doc.getString("description") ?: "",
                        amountSpent = doc.getLong("amountSpent")?.toDouble() ?: 0.0,
                        amountBudgeted = doc.getLong("amountBudgeted")?.toDouble() ?: 0.0
                    )
                }
                unfilteredCategories = categories
                val cat = filterCategoriesAccount()
                categoryAdapter.updateData(cat)
            }
            .addOnFailureListener { e -> // Catch the exception for logging
                Toast.makeText(this, "Error fetching data from Firestore: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                Log.e("CategoryView", "Error fetching Firestore categories", e) // Log the error
            }
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