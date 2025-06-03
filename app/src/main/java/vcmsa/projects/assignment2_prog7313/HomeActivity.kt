package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var walletValueTextView: TextView

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

        val btnBudgetPlan = findViewById<Button>(R.id.btnBudgetPlan)
        val btnMyGoals = findViewById<Button>(R.id.btnMyGoals)
        val btnDashboard = findViewById<Button>(R.id.btnDashboard)
        val plusSign = findViewById<TextView>(R.id.plusSign)
        walletValueTextView = findViewById(R.id.walletValue)

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
    }

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
}
