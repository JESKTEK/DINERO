package vcmsa.projects.assignment2_prog7313

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart

    private lateinit var ivDineroLogo: ImageView
    private lateinit var btnBack: ImageButton
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        pieChart = findViewById(R.id.pieChart)
        barChart = findViewById(R.id.barChart)
        lineChart = findViewById(R.id.lineChart)
        ivDineroLogo = findViewById(R.id.ivDineroLogo)
        btnBack = findViewById(R.id.btnBack)

        // OnClickListener for Dinero Logo to show logout menu
        ivDineroLogo.setOnClickListener { view ->
            showLogoutPopupMenu(view)
        }

        // OnClickListener for back button
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val spinner: Spinner = findViewById(R.id.chartTypeSpinner)
        val chartTypes = listOf("Budget Breakdown", "Monthly Expenses")

        completeGoalIfMatch(this, "Check your Dashboard")

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, chartTypes)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (chartTypes[position]) {
                    "Budget Breakdown" -> showPieChart()
                    "Monthly Expenses" -> showBarChart()
                    "Line" -> showLineChart()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        showPieChart()

        val navBar = findViewById<BottomNavigationView>(R.id.bottomNav)
        navBar.selectedItemId = R.id.dashboard
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
                    true
                }
                else -> {false}
            }
        }
    }

    private fun getEntries(): List<Entry> = listOf(
        Entry(0f, 40f),
        Entry(1f, 25f),
        Entry(2f, 20f),
        Entry(3f, 15f)
    )

/*
Title: Android Tutorial for Beginners: Create a Pie Chart With XML
Author: Ivanna Kacevica
Date: 17 August 2018
Availability: https://medium.com/@0xKartik/create-barchart-in-android-studio-14943339a211
*/
    private fun showPieChart() {
        pieChart.visibility = View.VISIBLE
        barChart.visibility = View.GONE
        lineChart.visibility = View.GONE

        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val userEmail = currentUser.email ?: return

        FirebaseFirestore.getInstance().collection("Categories")
            .whereEqualTo("emailAssociated", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                val entries = mutableListOf<PieEntry>()
                for (doc in documents) {
                    val name = doc.getString("catName") ?: "Unknown"
                    val spent = doc.getDouble("amountSpent") ?: 0.0
                    if (spent > 0) entries.add(PieEntry(spent.toFloat(), name))
                }

                val dataSet = PieDataSet(entries, " ")
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
                pieChart.data = PieData(dataSet)
                pieChart.centerText = "Budget Breakdown"
                pieChart.description.text = " "
                pieChart.setEntryLabelColor(Color.BLACK)
                pieChart.animateY(1500, Easing.EaseInOutCirc)
                pieChart.invalidate()
            }
    }


/*
Title: Create Barchart in Android Studio
Author: Kartik
Date: 21 June 2019
Availability: https://medium.com/@0xKartik/create-barchart-in-android-studio-14943339a211
*/

    private fun showBarChart() {
        pieChart.visibility = View.GONE
        barChart.visibility = View.VISIBLE
        lineChart.visibility = View.GONE

        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val userEmail = currentUser.email ?: return

        FirebaseFirestore.getInstance().collection("Expenses")
            .whereEqualTo("emailAssociated", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                val monthTotals = linkedMapOf<String, Float>()
                val sdf = java.text.SimpleDateFormat("d/M/yyyy", java.util.Locale.getDefault())
                val cal = java.util.Calendar.getInstance()

                val monthsToShow = (-2..2)
                //Maybe should change range if can't see in future? idk.
                val monthLabels = mutableListOf<String>()
                val monthKeys = mutableListOf<Pair<Int, Int>>()

                for (offset in monthsToShow) {
                    val tempCal = cal.clone() as java.util.Calendar
                    tempCal.add(java.util.Calendar.MONTH, offset)

                    val year = tempCal.get(java.util.Calendar.YEAR)
                    val month = tempCal.get(java.util.Calendar.MONTH)
                    val label = tempCal.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.SHORT, java.util.Locale.getDefault()) ?: "?"

                    monthLabels.add(label)
                    monthKeys.add(Pair(year, month))
                    monthTotals["$year-$month"] = 0f
                }


                for (doc in documents) {
                    val dateString = doc.getString("dateCreated") ?: continue
                    val amt = doc.getDouble("amountSpent")?.toFloat() ?: continue
                    if (amt < 0) continue

                    val date = try { sdf.parse(dateString) } catch (e: Exception) { null } ?: continue
                    val expCal = java.util.Calendar.getInstance().apply { time = date }

                    val year = expCal.get(java.util.Calendar.YEAR)
                    val month = expCal.get(java.util.Calendar.MONTH)
                    val key = "$year-$month"

                    if (monthTotals.containsKey(key)) {
                        monthTotals[key] = (monthTotals[key] ?: 0f) + amt
                    }
                }
                val entries = monthKeys.mapIndexed { index, (year, month) ->
                    val key = "$year-$month"
                    BarEntry(index.toFloat(), monthTotals[key] ?: 0f)
                }

                val dataSet = BarDataSet(entries, " ")
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
                dataSet.valueTextColor = Color.BLACK
                dataSet.valueTextSize = 14f

                val barData = BarData(dataSet)
                barData.barWidth = 0.9f

                barChart.data = barData
                barChart.setFitBars(true)
                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(monthLabels)
                barChart.xAxis.granularity = 1f
                barChart.description.text = ""
                barChart.xAxis.isGranularityEnabled = true
                barChart.axisLeft.axisMinimum = 0f
                barChart.axisRight.axisMinimum = 0f
                barChart.animateY(1500, Easing.EaseOutCirc)
                barChart.invalidate()
                val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val monthlyBudgetMin = prefs.getFloat("monthlyBudgetMin", 0f)
                val monthlyBudgetMax = prefs.getFloat("monthlyBudgetMax", 100000f)

                val leftAxis = barChart.axisLeft
                leftAxis.removeAllLimitLines()

                val minLine = LimitLine(monthlyBudgetMin, "Min Budget")
                minLine.lineColor = Color.GREEN
                minLine.lineWidth = 2f
                minLine.textColor = Color.DKGRAY
                minLine.textSize = 12f

                val maxLine = LimitLine(monthlyBudgetMax, "Max Budget")
                maxLine.lineColor = Color.RED
                maxLine.lineWidth = 2f
                maxLine.textColor = Color.DKGRAY
                maxLine.textSize = 12f

                leftAxis.addLimitLine(minLine)
                leftAxis.addLimitLine(maxLine)

            }
    }




    private fun showLineChart() {
        pieChart.visibility = View.GONE
        barChart.visibility = View.GONE
        lineChart.visibility = View.VISIBLE

        val entries = getEntries()
        val dataSet = LineDataSet(entries, "Expenses")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
        dataSet.setCircleColor(Color.BLACK)
        dataSet.valueTextSize = 12f
        lineChart.data = LineData(dataSet)

        lineChart.description.text = "Line Chart"

        lineChart.animateX(1000, Easing.EaseInOutCubic)

        lineChart.invalidate()
    }

    // Show the logout popup menu
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