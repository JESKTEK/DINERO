package vcmsa.projects.assignment2_prog7313

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
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
    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //database = AppDatabase.getInstance(this)
        //syncFirebaseToRoom()

        categoryAdapter = CategoryAdapter(emptyList())


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = categoryAdapter

        lifecycleScope.launch {
            loadCategories()
        }



        var datesInput1 = false
        var datesInput2 = false
        binding.fromDateSel.setOnClickListener() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.fromDateSel.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                if (datesInput1 && datesInput2) {
                    filterCategoriesDate(
                        binding.fromDateSel.text.toString(),
                        binding.toDateSel.text.toString()
                    )
                }
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
                if (datesInput1 && datesInput2) {
                    filterCategoriesDate(
                        binding.fromDateSel.text.toString(),
                        binding.toDateSel.text.toString()
                    )
                }
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
    private fun filterCategoriesDate(fromDateS: String, toDateS: String){


        val fromDate = convertStringDate(fromDateS)
        val toDate = convertStringDate(toDateS)

        filteredCategories = unfilteredCategories.filter { category ->
            val expenseDate : Date = convertStringDate(category.dateCreated)
            expenseDate.after(fromDate) && expenseDate.before(toDate)

        }

        categoryAdapter.updateData(filteredCategories)
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
                categoryAdapter.updateData(categories)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching data from Firestore", Toast.LENGTH_SHORT)
                    .show()
            }

        //val userDao = database.userDao()
        //val users = userDao.getAllUsers()
        //userAdapter.updateData(users)
    }

}