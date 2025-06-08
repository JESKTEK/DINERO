package vcmsa.projects.assignment2_prog7313

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_calculator : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var expressionText: TextView

    private var currentInput = ""
    private var operand1 = 0.0
    private var operator: String? = null
    private var resetInputOnNextDigit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calculator_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultText = findViewById(R.id.resultText)
        expressionText = findViewById(R.id.expressionText)

        val buttonGrid = findViewById<android.widget.GridLayout>(R.id.buttonGrid)
        for (i in 0 until buttonGrid.childCount) {
            val button = buttonGrid.getChildAt(i) as Button
            button.setOnClickListener { onButtonClick(button.text.toString()) }
        }
    }

    private fun onButtonClick(text: String) {
        when (text) {
            in "0".."9" -> {
                if (resetInputOnNextDigit) {
                    currentInput = ""
                    resetInputOnNextDigit = false
                }
                currentInput += text
                resultText.text = currentInput
            }
            "+", "−", "×", "÷" -> {
                operand1 = currentInput.toDoubleOrNull() ?: 0.0
                operator = text
                expressionText.text = "$operand1 $operator"
                resetInputOnNextDigit = true
            }
            "=" -> {
                val operand2 = currentInput.toDoubleOrNull() ?: return
                val result = when (operator) {
                    "+" -> operand1 + operand2
                    "−" -> operand1 - operand2
                    "×" -> operand1 * operand2
                    "÷" -> if (operand2 != 0.0) operand1 / operand2 else Double.NaN
                    else -> Double.NaN
                }

                val displayResult = if (result.isNaN()) "Error" else result.toString()
                expressionText.text = "$operand1 $operator $operand2"
                resultText.text = displayResult
                currentInput = displayResult
                resetInputOnNextDigit = true
            }
            "C" -> {
                operand1 = 0.0
                operator = null
                currentInput = ""
                expressionText.text = ""
                resultText.text = "0"
            }
        }
    }
}
