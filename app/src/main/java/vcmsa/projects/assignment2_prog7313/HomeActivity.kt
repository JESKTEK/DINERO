package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle

import android.view.View

import android.util.Log

import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import vcmsa.projects.assignment2_prog7313.databinding.ActivityHomeBinding
import android.widget.TextView

import java.util.Calendar
import java.text.SimpleDateFormat


class HomeActivity : AppCompatActivity() {

    private lateinit var walletValueTextView: TextView
    private lateinit var auth: FirebaseAuth

    private val firestore = FirebaseFirestore.getInstance()
    private val userEmail get() = FirebaseAuth.getInstance().currentUser?.email ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val btnBudgetPlan = findViewById<Button>(R.id.btnBudgetPlan)
        val btnMyGoals = findViewById<Button>(R.id.btnMyGoals)
        val btnDashboard = findViewById<Button>(R.id.btnDashboard)
        val plusSign = findViewById<TextView>(R.id.plusSign)

        walletValueTextView = findViewById(R.id.walletValue)
        val dineroLogoLogoutIcon = findViewById<ImageView>(R.id.dineroLogoClickable)

        dineroLogoLogoutIcon.setOnClickListener { view ->
            showLogoutPopupMenu(view)
        }

        btnBudgetPlan.setOnClickListener {
            startActivity(Intent(this, BudgetHomePageActivity::class.java))
        }

        btnDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        btnMyGoals.setOnClickListener {
            startActivity(Intent(this, Goals::class.java))
        }

        plusSign.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        generateWeeklyGoals(userEmail, getCurrentWeekId())

    }

    //Goals Code - It needs to be here in case the user skips the goal page.
    //NOTE - WE NEED TO ADD GOAL 5 WHEN REVIEW BUDGET IS ADDED

    val allGoals = listOf(
        Goal("1", "Add an Expense", "Track a new expense: we won't judge.", false),
        Goal("2", "Check your Dashboard", "View your dashboard to make sure you're on track!", false),
        Goal("3", "Add a Category", "Create a new category to enhance your budgeting!", false),
        Goal("4", "Fill your Wallet", "Add some money to your wallet!", false)//,
        //Goal("5", "Review your Week", "Review your weekly summary to make sure you're on track.", false)
    )

    fun generateWeeklyGoals(userId: String, weekId: String) {
        val weeklyGoalsCollection = FirebaseFirestore.getInstance()
            .collection("Users").document(userId)
            .collection("weeklyGoals")

        val currentWeekStartDate = getCurrentWeekStart()

        weeklyGoalsCollection.document(weekId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    val selectedGoals = allGoals.shuffled().take(3)
                    val weeklyGoalsData = WeeklyGoals(selectedGoals, currentWeekStartDate)

                    weeklyGoalsCollection.document(weekId)
                        .set(weeklyGoalsData)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Goals set successfully for the new week.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to set goals for the week.", e)
                        }
                } else {
                    Log.d(
                        "Firestore",
                        "Weekly goals already exist, no new goals generated."
                    )
                }
            }.addOnFailureListener { e ->
                Log.e("Firestore", "Failed to check for existing weekly goals.", e)
            }
    }


    fun getCurrentWeekStart(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    //End of Goal Code


    override fun onResume() {
        super.onResume()
        loadWalletBalanceFromFirebase()
    }

    private fun loadWalletBalanceFromFirebase() {
        if (userEmail.isEmpty()) {
            walletValueTextView.text = "R0.00"
            return
        }

        firestore.collection("Users").document(userEmail).get()
            .addOnSuccessListener { userDoc ->
                val walletId = userDoc.getString("walletId")

                if (walletId != null) {
                    firestore.collection("Wallets").document(walletId).get()
                        .addOnSuccessListener { walletDoc ->
                            val balance = walletDoc.getDouble("balance") ?: 0.0
                            walletValueTextView.text = "R%.2f".format(balance)
                        }
                        .addOnFailureListener {
                            walletValueTextView.text = "R0.00"
                            Toast.makeText(this, "Could not fetch wallet", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    walletValueTextView.text = "R0.00"
                }
            }
            .addOnFailureListener {
                walletValueTextView.text = "R0.00"
                Toast.makeText(this, "User wallet not found", Toast.LENGTH_SHORT).show()
            }
    }

    // show the logout popup menu
    private fun showLogoutPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.logout_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_chatbot -> {
                    val intent = Intent(this, ChatbotActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.calculator_layout -> {
                    val intent = Intent(this, activity_calculator::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_logout -> {
                    // Sign out the user
                    auth.signOut()
                    Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()

                    // Redirect to LoginActivity and clear activity stack
                    val intent = Intent(this, LoginActivity::class.java) // Assuming LoginActivity is your login screen
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clears back stack
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}