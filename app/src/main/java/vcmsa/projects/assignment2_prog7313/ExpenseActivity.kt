package vcmsa.projects.assignment2_prog7313

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.io.ByteArrayOutputStream
import java.util.*

class ExpenseActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore
    private var catId: String? = null
    private var catName: String? = null

    private lateinit var camLauncher: ActivityResultLauncher<Void?>
    private var chosenImageUri: Uri? = null
    private var chosenImageBitmap: Bitmap? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                chosenImageUri = uri
                findViewById<ImageButton>(R.id.imageInput).setImageURI(uri)
                Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {
            chosenImageUri = data?.data!!
            findViewById<ImageButton>(R.id.imageInput).setImageURI(chosenImageUri)
        }
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }


    private fun convertImageToBase64(imageButton: android.widget.ImageButton): String? {
        val drawable = imageButton.drawable ?: return ""
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        camLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                chosenImageBitmap = bitmap
                findViewById<ImageButton>(R.id.imageInput).setImageBitmap(bitmap)
                chosenImageUri = null
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
        /*****
        Title: SeekBar in Kotlin
        Author: GeeksforGeeks
        Date: 18 April 2025
        Availability: https://www.geeksforgeeks.org/seekbar-in-kotlin/
         *****/
        // Set default SeekBar value
        labelLimitValue.text = "R${seekBar.progress}.00"

        // Amount changes as seek bar moves
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                labelLimitValue.text = "R$value.00"
                // Clear custom amount if user uses SeekBar
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<ImageButton>(R.id.imageInput).setOnClickListener {
            //pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            camLauncher.launch(null)
        }



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
        btnAddExpense.setOnClickListener {
            val name = inputName.text.toString().trim()
            val date = inputDate.text.toString().trim()
            val emailAssociated = getCurrentUserEmail()
            val amountSpent = (labelLimitValue.text.toString().substring(1)).trim().toDouble()
            val description = inputDescription.text.toString().trim()

            if (chosenImageBitmap == null) {
                Toast.makeText(this, "Please take an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } //Error strategically placed to not trigger function and cause crash.
            val image = convertImageToBase64(findViewById<ImageButton>(R.id.imageInput))


            // error - nothing filled in
            if (name.isEmpty() || date.isEmpty() || description.isEmpty() || amountSpent == 0.00) {
                Toast.makeText(
                    this,
                    "Please fill in all fields, and set an expense above R0.00",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (catId != null){
                firestore.collection("Categories").document(catId!!).get()
                    .addOnSuccessListener {
                        document -> if (document.exists()) {
                        val currentAmtSpent = document.getDouble("amountSpent") ?: 0.0
                        val newAmtSpent = currentAmtSpent + amountSpent
                        firestore.collection("Categories").document(catId!!).update("amountSpent", newAmtSpent)
                            .addOnSuccessListener { d -> Log.d("Category", "Amount spent updated successfully.") }
                            .addOnFailureListener { e -> Log.w("Category", "Error updating amount spent.", e) }
                        }
                    }
            }

            val expense = hashMapOf(
                "catId" to catId,
                "categoryName" to catName,
                "itemName" to name,
                "uploadImage" to image,
                "dateCreated" to date,
                "amountSpent" to amountSpent,
                "details" to description,
                "emailAssociated" to emailAssociated
            )

            firestore.collection("Expenses")
                .add(expense)
                .addOnSuccessListener { documentReference ->
                    val documentId = documentReference.id
                    Toast.makeText(this, "Expense Logged Successfully.", Toast.LENGTH_SHORT).show()


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
