package vcmsa.projects.assignment2_prog7313

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.util.*

class ExpenseActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private var catId: String? = null
    private var catName: String? = null

    private lateinit var camLauncher: ActivityResultLauncher<Void?>
    private var chosenImageBitmap: Bitmap? = null

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        camLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                chosenImageBitmap = bitmap
                findViewById<ImageButton>(R.id.imageInput).setImageBitmap(bitmap)
            }
        }

        catId = intent.getStringExtra("catId")
        catName = intent.getStringExtra("catName")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutCreateExpense)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Link views
        val inputName = findViewById<EditText>(R.id.inputName)
        val inputDate = findViewById<EditText>(R.id.inputDate)
        val inputDescription = findViewById<EditText>(R.id.inputDescription)
        val labelCharCount = findViewById<TextView>(R.id.labelCharCount)
        val seekBar = findViewById<SeekBar>(R.id.inputLimitSeekBar)
        val labelLimitValue = findViewById<TextView>(R.id.labelLimitValue)
        val imageInput = findViewById<ImageButton>(R.id.imageInput)
        val btnAddExpense = findViewById<Button>(R.id.AddExpenseBtn)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            startActivity(Intent(this, CategoryView::class.java))
            finish()
        }

        labelLimitValue.text = "R${seekBar.progress}.00"
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                labelLimitValue.text = "R$value.00"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        imageInput.setOnClickListener {
            camLauncher.launch(null)
        }

        inputDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                inputDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day)

            datePicker.show()
        }

        inputDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                labelCharCount.text = "${s?.length ?: 0}/250"
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnAddExpense.setOnClickListener {
            val name = inputName.text.toString().trim()
            val date = inputDate.text.toString().trim()
            val description = inputDescription.text.toString().trim()
            val amountSpent = (labelLimitValue.text.toString().substring(1)).trim().toDouble()
            val email = FirebaseAuth.getInstance().currentUser?.email ?: return@setOnClickListener

            if (name.isEmpty() || date.isEmpty() || description.isEmpty() || amountSpent <= 0.0) {
                Toast.makeText(this, "Please fill in all fields and set a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userRef = firestore.collection("Users").document(email)
            userRef.get().addOnSuccessListener { userDoc ->
                val walletId = userDoc.getString("walletId") ?: return@addOnSuccessListener
                val walletRef = firestore.collection("Wallets").document(walletId)

                walletRef.get().addOnSuccessListener { walletDoc ->
                    val currentBalance = walletDoc.getDouble("balance") ?: 0.0

                    if (amountSpent > currentBalance) {
                        Toast.makeText(this, "Insufficient wallet balance!", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    // Proceed with logging the expense
                    val image = if (chosenImageBitmap != null) convertImageToBase64(imageInput) else null

                    if (catId != null) {
                        firestore.collection("Categories").document(catId!!).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val currentAmtSpent = document.getDouble("amountSpent") ?: 0.0
                                    val newAmtSpent = currentAmtSpent + amountSpent
                                    firestore.collection("Categories").document(catId!!).update("amountSpent", newAmtSpent)
                                }
                            }
                    }

                    val expense = hashMapOf(
                        "catId" to catId,
                        "categoryName" to catName,
                        "itemName" to name,
                        "dateCreated" to date,
                        "amountSpent" to amountSpent,
                        "details" to description,
                        "emailAssociated" to email
                    )
                    if (image != null) expense["uploadImage"] = image

                    firestore.collection("Expenses")
                        .add(expense)
                        .addOnSuccessListener { documentReference ->
                            val updateData = hashMapOf("id" to documentReference.id)
                            documentReference.update(updateData as Map<String, Any>)

                            // Deduct from wallet
                            val newBalance = currentBalance - amountSpent
                            walletRef.update("balance", newBalance)

                            Toast.makeText(this, "Expense logged & wallet updated!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, BudgetHomePageActivity::class.java))
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to log expense.", Toast.LENGTH_SHORT).show()
                        }

                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to load wallet.", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(this, "Failed to fetch user wallet info.", Toast.LENGTH_SHORT).show()
            }
        }




        val navBar = findViewById<BottomNavigationView>(R.id.bottomNav)
        navBar.selectedItemId = R.id.budget
        navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.budget -> {
                    val intent = Intent(this, BudgetHomePageActivity::class.java)
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

    private fun convertImageToBase64(imageButton: ImageButton): String? {
        val drawable = imageButton.drawable ?: return null
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
