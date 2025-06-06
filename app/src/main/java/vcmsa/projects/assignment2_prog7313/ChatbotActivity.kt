package vcmsa.projects.assignment2_prog7313

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChatbotActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatbot)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupFAQ()
    }

    private fun setupFAQ() {
        val faqContainer = findViewById<LinearLayout>(R.id.faqContainer)

        // Sample FAQ data (question -> answer)
        val faqList = listOf(
            "How can I contact support?" to "You can contact support via the ChatBot or email support@example.com.",
            "Where can I find my transaction history?" to "Your transaction history is located in the Home Page under '+'.",
            "How do I create a new budget?" to "Tap on the Home tab and select 'Create Budget' to start."
        )

        // Clear existing views if any
        faqContainer.removeAllViews()

        // Create Q&A views dynamically
        faqList.forEach { (question, answer) ->
            // Answer TextView — must be declared before the question's click listener to capture it
            val answerView = TextView(this).apply {
                text = answer
                textSize = 16f
                setPadding(40, 10, 20, 20)
                visibility = View.GONE // Initially hidden
            }

            // Question TextView
            val questionView = TextView(this).apply {
                text = question
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setPadding(20, 20, 20, 20)
                setBackgroundColor(0xFFE0E0E0.toInt()) // Light gray background for question
                setOnClickListener {
                    // Toggle visibility of answer when question clicked
                    answerView.visibility = if (answerView.visibility == View.GONE) View.VISIBLE else View.GONE
                }
            }

            // Add question and answer to container
            faqContainer.addView(questionView)
            faqContainer.addView(answerView)
        }
    }
}
