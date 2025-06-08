package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class ChatbotActivity : AppCompatActivity() {

    private lateinit var chatContainer: LinearLayout
    private lateinit var userOptions: List<String>
    private lateinit var scrollView: ScrollView
    private lateinit var inputSection: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var sendButton: Button
    private var firstInteraction = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatbot)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        chatContainer = findViewById(R.id.faqContainer)
        scrollView = findViewById(R.id.chatScrollView)
        inputSection = findViewById(R.id.inputSection)

        userOptions = listOf(
            "Add a Budget",
            "View Existing Budgets",
            "Check Transaction History",
            "Contact Support"
        )

        setupInputArea()
        showOptions()
    }

    private fun setupInputArea() {
        inputSection.removeAllViews()

        inputEditText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            hint = "Enter option number"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        sendButton = Button(this).apply {
            text = "Send"
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                val userInput = inputEditText.text.toString().trim()
                if (userInput.isEmpty()) {
                    Toast.makeText(this@ChatbotActivity, "Please enter a number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val optionIndex = try {
                    userInput.toInt() - 1
                } catch (e: NumberFormatException) {
                    -1
                }

                if (optionIndex in userOptions.indices) {
                    handleUserSelection(optionIndex)
                    inputEditText.text.clear()
                } else {
                    Toast.makeText(this@ChatbotActivity, "Invalid option. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        inputSection.addView(inputEditText)
        inputSection.addView(sendButton)
    }

    private fun showOptions() {
        val message = if (firstInteraction) {
            firstInteraction = false
            "Hi! How can I assist you today? Please select one of the options below:\n" +
                    userOptions.mapIndexed { index, option -> "${index + 1}. $option" }.joinToString("\n")
        } else {
            "Please make a selection:\n" +
                    userOptions.mapIndexed { index, option -> "${index + 1}. $option" }.joinToString("\n")
        }

        addBotMessage(message)
    }

    private fun handleUserSelection(index: Int) {
        addUserMessage(userOptions[index])

        val response = when (index) {
            0 -> "To add a budget, tap on the 'Home' tab and select 'Create Budget'."
            1 -> "To view budgets, go to the 'Budgets' tab where all your budgets are listed."
            2 -> "Your transaction history is available on the 'Home' page under the '+' section."
            3 -> "You can contact support at support@example.com or by using this chatbot."
            else -> "Sorry, I didn't understand that option."
        }

        addBotMessage(response)
        showOptions()
    }

    private fun addBotMessage(message: String) {
        val messageLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.START
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(10, 10, 50, 10)
            }
        }

        val textView = TextView(this).apply {
            text = message
            textSize = 16f
            setPadding(24, 16, 24, 16)
            setBackgroundResource(R.drawable.bubble_bot)
        }

        messageLayout.addView(textView)
        chatContainer.addView(messageLayout)
        scrollToBottom()
    }

    private fun addUserMessage(message: String) {
        val messageLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.END // Change from END to START to align left
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(10, 10, 50, 10)
            }
        }

        val textView = TextView(this).apply {
            text = message
            textSize = 16f
            setPadding(24, 16, 24, 16)
            setBackgroundResource(R.drawable.bubble_user) // keep your bubble drawable
        }

        messageLayout.addView(textView)
        chatContainer.addView(messageLayout)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}
