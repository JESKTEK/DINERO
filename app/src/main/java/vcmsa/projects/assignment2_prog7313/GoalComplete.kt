package vcmsa.projects.assignment2_prog7313

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.util.Calendar

fun getCurrentWeekId(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val week = calendar.get(Calendar.WEEK_OF_YEAR)
    return "Year_$year-Week_$week"
}

fun completeGoalIfMatch(context: Context, goalName: String, onSuccess: (() -> Unit)? = null) {
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
    val weekId = getCurrentWeekId()

    val docRef = Firebase.firestore
        .collection("Users").document(userEmail)
        .collection("weeklyGoals").document(weekId)

    docRef.get().addOnSuccessListener { document ->
        val goals = document["goals"] as? List<Map<String, Any>> ?: return@addOnSuccessListener
        val updatedGoals = goals.map {
            if (it["goalName"] == goalName && it["goalCompleted"] == false) {
                it.toMutableMap().apply { this["goalCompleted"] = true }
            } else it
        }
        if (goals != updatedGoals) {
            docRef.update("goals", updatedGoals)
                .addOnSuccessListener {
                    incrementUserScore(userEmail)
                    AlertDialog.Builder(context)
                        .setTitle("🎉 Goal Complete!")
                        .setMessage("You've completed a goal!")
                        .setPositiveButton("Nice!") { dialog, _ -> dialog.dismiss() }
                        .show()
                    onSuccess?.invoke()
                }
        }
    }
}

/*
Title: Transactions and batched writes
Author: Google
Date: 04 June 2025
Availability: https://firebase.google.com/docs/firestore/manage-data/transactions
*/
fun incrementUserScore(userEmail: String) {
    val userRef = Firebase.firestore.collection("Users").document(userEmail)

    Firebase.firestore.runTransaction { transaction ->
        val snapshot = transaction.get(userRef)
        val currentScore = (snapshot["score"] as? Long ?: 0L).toInt()
        val currentLevel = (snapshot["level"] as? Long ?: 1L).toInt()

        val newScore = currentScore + 1
        val newLevel = 1 + (newScore / 3)

        transaction.update(userRef, mapOf(
            "score" to newScore,
            "level" to newLevel
        ))
    }.addOnSuccessListener {
        Log.d("Score", "Score was successfully updated.")
    }.addOnFailureListener {
        Log.e("Score", "Failed to change score.", it)
    }
}