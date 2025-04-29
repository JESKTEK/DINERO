package vcmsa.projects.assignment2_prog7313

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutCreateCategory)) { v, insets ->
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
        val inputCustomAmount = findViewById<EditText>(R.id.inputCustomAmount)
        val btnAddBudget = findViewById<Button>(R.id.AddBudgetbtn)

        // Set default SeekBar value
        labelLimitValue.text = "R${seekBar.progress}.00"

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

        // Calender functinality
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

        // gather all info
        btnAddBudget.setOnClickListener {
            val name = inputName.text.toString().trim()
            val date = inputDate.text.toString().trim()
            val description = inputDescription.text.toString().trim()
            val customAmount = inputCustomAmount.text.toString().trim()

            val finalAmount = if (customAmount.isNotEmpty()) {
                "R$customAmount"
            } else {
                labelLimitValue.text.toString()
            }

            // error
            if (name.isEmpty() || date.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //  Kian please replace with DB/firestore save or new screen later
            Toast.makeText(
                this,
                "Category Created:\nName: $name\nDate: $date\nAmount: $finalAmount",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
