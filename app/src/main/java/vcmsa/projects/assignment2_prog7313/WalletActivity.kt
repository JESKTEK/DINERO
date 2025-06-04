package vcmsa.projects.assignment2_prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class WalletActivity : AppCompatActivity() {

    private lateinit var currentBalanceTextView: TextView
    private lateinit var amountEditText: EditText
    private lateinit var addButton: Button
    private lateinit var transactionListView: ListView

    private var currentBalance = 0.0
    private val transactionList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private val firestore = FirebaseFirestore.getInstance()
    private val userEmail get() = FirebaseAuth.getInstance().currentUser?.email ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentBalanceTextView = findViewById(R.id.currentBalance)
        amountEditText = findViewById(R.id.amountEditText)
        addButton = findViewById(R.id.addButton)
        transactionListView = findViewById(R.id.transactionListView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transactionList)
        transactionListView.adapter = adapter

        getOrCreateWallet()





        val navBar = findViewById<BottomNavigationView>(R.id.bottomNav)
        navBar.selectedItemId = R.id.home
        navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeActivity::class.java)
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

    private fun getOrCreateWallet() {
        val userRef = firestore.collection("Users").document(userEmail)

        userRef.get().addOnSuccessListener { userDoc ->
            val walletId = userDoc.getString("walletId")

            if (walletId != null) {
                loadWallet(walletId)
            } else {
                val walletData = mapOf("balance" to 0.0)
                firestore.collection("Wallets").add(walletData)
                    .addOnSuccessListener { walletDoc ->
                        val newWalletId = walletDoc.id
                        userRef.set(mapOf("walletId" to newWalletId), SetOptions.merge())
                        loadWallet(newWalletId)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to create wallet.", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get user.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadWallet(walletId: String) {
        val walletRef = firestore.collection("Wallets").document(walletId)
        val transactionsRef = walletRef.collection("Transactions")

        walletRef.get().addOnSuccessListener { doc ->
            currentBalance = doc.getDouble("balance") ?: 0.0
            currentBalanceTextView.text = "R%.2f".format(currentBalance)

            loadTransactionHistory(transactionsRef)
            addButton.setOnClickListener {
                val input = amountEditText.text.toString()
                val amount = input.toDoubleOrNull()

                if (amount != null && amount > 0) {
                    currentBalance += amount
                    currentBalanceTextView.text = "R%.2f".format(currentBalance)
                    amountEditText.text.clear()

                    walletRef.update("balance", currentBalance)

                    val transactionData = mapOf(
                        "amount" to amount,
                        "timestamp" to Timestamp.now(),
                        "type" to " "
                    )

                    transactionsRef.add(transactionData)
                        .addOnSuccessListener {
                            transactionList.add(0,"R%.2f".format(amount))
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to save transaction.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load wallet.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadTransactionHistory(transactionsRef: com.google.firebase.firestore.CollectionReference) {
        transactionsRef.orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                transactionList.clear()
                for (doc in snapshot) {
                    val amount = doc.getDouble("amount") ?: 0.0
                    val type = doc.getString("type") ?: " "
                    transactionList.add("R%.2f".format(amount))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Could not load transactions", Toast.LENGTH_SHORT).show()
            }
    }
} 