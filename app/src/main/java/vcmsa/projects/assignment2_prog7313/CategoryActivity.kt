package vcmsa.projects.assignment2_prog7313

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.util.*

class CategoryActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutCreateCategory)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputName = findViewById<EditText>(R.id.inputName)
        val inputDate = findViewById<EditText>(R.id.inputDate)
        val inputDescription = findViewById<EditText>(R.id.inputDescription)
        val labelCharCount = findViewById<TextView>(R.id.labelCharCount)
        val seekBar = findViewById<SeekBar>(R.id.inputLimitSeekBar)
        val labelLimitValue = findViewById<TextView>(R.id.labelLimitValue)
        val inputCustomAmount = findViewById<EditText>(R.id.inputCustomAmount)
        val btnAddBudget = findViewById<Button>(R.id.AddBudgetbtn)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, BudgetHomePageActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Set default SeekBar value
        labelLimitValue.text = "R${seekBar.progress}.00"

        /*****
        Title: SeekBar in Kotlin
        Author: GeeksforGeeks
        Date: 18 April 2025
        Availability: https://www.geeksforgeeks.org/seekbar-in-kotlin/
         *****/
        // Amount changes as seek bar moves
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                labelLimitValue.text = "R$value.00"
                // Clear custom amount if user uses SeekBar
                if (fromUser) inputCustomAmount.text.clear()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        /*****
        Title: How to Implement DatePickerDialog in Android Using Kotlin
        Author: Abhishek Suman
        Date: 22 April 2025
        Availability: https://medium.com/%40abhisheksuman413/how-to-implement-datepickerdialog-in-android-using-kotlin-45c413e47464
         *****/
        // Calender functionality
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

        // Description character counter
        inputDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                labelCharCount.text = "${s?.length ?: 0}/250"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        fun getCurrentUserEmail(): String? {
            val user = FirebaseAuth.getInstance().currentUser
            return user?.email
        }

        // gather all info
        btnAddBudget.setOnClickListener {
            val name = inputName.text.toString().trim()
            val date = inputDate.text.toString().trim()
            val emailAssociated = getCurrentUserEmail()
            val amountSpent = 0.00;
            val description = inputDescription.text.toString().trim()
            val customAmount = inputCustomAmount.text.toString().trim()

            val finalAmount = if (customAmount.isNotEmpty()) {
                customAmount.toDouble()
            } else {
                (labelLimitValue.text.toString().substring(1)).trim().toDouble()
            }

            // error
            if (name.isEmpty() || date.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = hashMapOf(
                "catName" to name,
                "dateCreated" to date,
                "emailAssociated" to emailAssociated,
                "description" to description,
                "amountSpent" to amountSpent,
                "amountBudgeted" to finalAmount
            )

            firestore.collection("Categories")
                .add(category)
                .addOnSuccessListener { documentReference ->
                    val documentId = documentReference.id
                    Toast.makeText(this, "Category Made Successfully.", Toast.LENGTH_SHORT).show()

                    completeGoalIfMatch(this, "Add a Category")

                    val updateData = hashMapOf("id" to documentId)
                    documentReference.update(updateData as Map<String, Any>)

                    val intent = Intent(this, BudgetHomePageActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error Making Category.", Toast.LENGTH_SHORT)
                        .show()
                }

            }


        }

    }

