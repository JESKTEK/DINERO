package vcmsa.projects.assignment2_prog7313

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle


import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast

import android.util.AttributeSet
import android.util.Log
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.ProgressBar
import android.widget.TextView


import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

import vcmsa.projects.assignment2_prog7313.databinding.ActivityGoalsBinding

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.util.Calendar

class Goals : AppCompatActivity() {
  
    private lateinit var binding: ActivityGoalsBinding
    private lateinit var ivDineroLogo: ImageView

    private val firestore = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private lateinit var goal1Title: TextView
    private lateinit var goal1Desc: TextView
    private lateinit var goal1Status: CheckBox

    private lateinit var goal2Title: TextView
    private lateinit var goal2Desc: TextView
    private lateinit var goal2Status: CheckBox

    private lateinit var goal3Title: TextView
    private lateinit var goal3Desc: TextView
    private lateinit var goal3Status: CheckBox

    private lateinit var levelRank: TextView
    private lateinit var levelScore: TextView
    private lateinit var levelBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        ivDineroLogo = binding.ivDineroLogo
        ivDineroLogo.setOnClickListener { view ->
            showLogoutPopupMenu(view)
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, BudgetHomePageActivity::class.java)
            startActivity(intent)
            finish()
        }


        val navBar = findViewById<BottomNavigationView>(R.id.bottomNav)
        navBar.selectedItemId = R.id.goals
        navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.goals -> {
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

    // Show the logout popup menu
    private fun showLogoutPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.logout_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    // Sign out the user
                    auth.signOut()
                    Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
        popup.show()

        
        
        levelRank = findViewById(R.id.rankText)
        levelScore = findViewById(R.id.scoreText)
        levelBar = findViewById(R.id.levelProgressBar)

        goal1Title = findViewById(R.id.Goal1Title)
        goal1Desc = findViewById(R.id.Goal1Description)
        goal1Status = findViewById(R.id.Goal1Status)

        goal2Title = findViewById(R.id.Goal2Title)
        goal2Desc = findViewById(R.id.Goal2Description)
        goal2Status = findViewById(R.id.Goal2Status)


        updateCurrentGoals()

        goal3Title = findViewById(R.id.Goal3Title)
        goal3Desc = findViewById(R.id.Goal3Description)
        goal3Status = findViewById(R.id.Goal3Status)
        updateScore()




    }
    fun setGoalCompleted(titleView: TextView, descView: TextView) {
        val greenColour = android.graphics.Color.parseColor("#19c219")

        titleView.setTextColor(greenColour)
        descView.setTextColor(greenColour)
    }


    fun updateScore() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
        firestore.collection("Users").document(userEmail).get().addOnSuccessListener { document ->
            val score = document.getLong("score")?.toInt() ?: 0
            val level = score / 3
            val quotient = score % 3

            levelBar.max = 3
            levelBar.progress = quotient

            levelScore.text = "${3 - quotient} Tasks to Level Up"

            val ranks = listOf("BRONZE" to "#CD7F32", "SILVER" to "#C0C0C0",
                "GOLD" to "#FFD700", "PLATINUM" to "#F2FDFF", "EMERALD" to "#50C878",
                "DIAMOND" to "#79D3E8")
            val rankIndex = level.coerceAtMost(ranks.lastIndex)
            val (rankName, colorHex) = ranks[rankIndex]
            levelRank.text = rankName
            levelRank.setTextColor(android.graphics.Color.parseColor(colorHex))
        }
    }


    fun updateCurrentGoals() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
        val weekId = getCurrentWeekId()

        val docRef = firestore
            .collection("Users").document(userEmail)
            .collection("weeklyGoals").document(weekId)

        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val goals = document["goals"] as? List<Map<String, Any>>
                if (goals != null && goals.size >= 3) {

                    goal1Title.text = goals[0]["goalName"]?.toString()?: "Placeholder"
                    goal1Desc.text = goals[0]["goalDescription"]?.toString()?: "Placeholder 2"
                    goal1Status.isChecked = (goals[0]["goalCompleted"] as Boolean)
                    if (goals[0]["goalCompleted"] as Boolean) {
                        setGoalCompleted(goal1Title, goal1Desc)
                    }

                    goal2Title.text = goals[1]["goalName"]?.toString()?: "Placeholder"
                    goal2Desc.text = goals[1]["goalDescription"]?.toString()?: "Placeholder 2"
                    goal2Status.isChecked = (goals[1]["goalCompleted"] as Boolean)
                    if (goals[1]["goalCompleted"] as Boolean) {
                        setGoalCompleted(goal2Title, goal2Desc)
                    }

                    goal3Title.text = goals[2]["goalName"]?.toString()?: "Placeholder"
                    goal3Desc.text = goals[2]["goalDescription"]?.toString()?: "Placeholder 2"
                    goal3Status.isChecked = (goals[2]["goalCompleted"] as Boolean)
                    if (goals[2]["goalCompleted"] as Boolean) {
                        setGoalCompleted(goal3Title, goal3Desc)
                    }
                }
            } else {
                Log.d("Goals", "Error fetching weekly goals for this week. ")
            }
        }.addOnFailureListener {
            Log.e("Goals", "Error fetching weekly goals for this week. ", it)
        }
    }

}