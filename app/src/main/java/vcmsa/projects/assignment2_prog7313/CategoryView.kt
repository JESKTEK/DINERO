package vcmsa.projects.assignment2_prog7313

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

class CategoryView : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryViewBinding
    private lateinit var categoryAdapter: CategoryAdapter
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
            val dateCreated = result.data?.getStringExtra("dateCreated") ?: return@registerForActivityResult
            val emailAssociated = result.data?.getStringExtra("emailAssociated") ?: return@registerForActivityResult
            val amountSpent = result.data?.getLongExtra("amountSpent", 0) ?: return@registerForActivityResult
            val amountBudgeted = result.data?.getLongExtra("amountBudgeted", 0) ?: return@registerForActivityResult

            lifecycleScope.launch {
                val category = Category(id = id, catName = catName, dateCreated = dateCreated, emailAssociated = emailAssociated, amountSpent = amountSpent, amountBudgeted = amountBudgeted)
                //database.postDao().insertPost(newPost)
                loadCategories()
            }
        }

    }

    private suspend fun loadCategories() {
        firestore.collection("Categories").get()
            .addOnSuccessListener { snapshot ->
                val posts  = snapshot.documents.map { doc ->

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
                        amountSpent = doc.getLong("Likes")?.toLong() ?: 0,
                        amountBudgeted = doc.getLong("amountBudgeted")?.toLong() ?: 0
                    )
                }
                categoryAdapter.updateData(posts)
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