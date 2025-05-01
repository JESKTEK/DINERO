package vcmsa.projects.assignment2_prog7313

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import vcmsa.projects.assignment2_prog7313.databinding.ActivityCategoryViewBinding
import vcmsa.projects.assignment2_prog7313.databinding.ActivityExpenseViewBinding
import java.io.ByteArrayInputStream
import kotlin.io.encoding.Base64

class ExpenseView : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseViewBinding
    private lateinit var expenseAdapter: ExpenseAdapter


    //private lateinit var database: AppDatabase
    private val firestore = Firebase.firestore


    private var unfilteredExpenses: List<Expense> = emptyList()
    private var filteredExpenses: List<Expense> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpenseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //database = AppDatabase.getInstance(this)
        //syncFirebaseToRoom()

        expenseAdapter = ExpenseAdapter(emptyList())


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = expenseAdapter

        lifecycleScope.launch {
            loadExpenses()
        }

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
                expenseAdapter.updateData(expenses)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching data from Firestore", Toast.LENGTH_SHORT)
                    .show()
            }

    }

}