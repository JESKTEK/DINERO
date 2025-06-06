package vcmsa.projects.assignment2_prog7313

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
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

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

        // initialise Firebase Auth
        auth = FirebaseAuth.getInstance()

        pieChart = findViewById(R.id.pieChart)
        barChart = findViewById(R.id.barChart)
        lineChart = findViewById(R.id.lineChart)
        ivDineroLogo = findViewById(R.id.ivDineroLogo)
        btnBack = findViewById(R.id.btnBack)

        // Set OnClickListener for Dinero Logo to show logout menu
        ivDineroLogo.setOnClickListener { view ->
            showLogoutPopupMenu(view)
        }

        // Set OnClickListener for back button
        btnBack.setOnClickListener {
            val intent = Intent(this, BudgetHomePageActivity::class.java)
            startActivity(intent)
            finish()
        }

        val spinner: Spinner = findViewById(R.id.chartTypeSpinner)
        val chartTypes = listOf("Pie", "Bar", "Line")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, chartTypes)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (chartTypes[position]) {
                    "Pie" -> showPieChart()
                    "Bar" -> showBarChart()
                    "Line" -> showLineChart()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        showPieChart() // Show by default

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

    private fun showPieChart() {
        pieChart.visibility = View.VISIBLE
        barChart.visibility = View.GONE
        lineChart.visibility = View.GONE

        val entries = listOf(
            PieEntry(40f, "Food"),
            PieEntry(25f, "Transport"),
            PieEntry(20f, "Entertainment"),
            PieEntry(15f, "Other")
        )

        val dataSet = PieDataSet(entries, "Expenses")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
        pieChart.data = PieData(dataSet)

        pieChart.centerText = "Spending"
        pieChart.description.text = "Pie Chart"
        pieChart.setEntryLabelColor(Color.BLACK)

        pieChart.animateY(1000, Easing.EaseInOutQuad)

        pieChart.invalidate()
    }

    private fun showBarChart() {
        pieChart.visibility = View.GONE
        barChart.visibility = View.VISIBLE
        lineChart.visibility = View.GONE

        val entries = listOf(
            BarEntry(0f, 40f),
            BarEntry(1f, 25f),
            BarEntry(2f, 20f),
            BarEntry(3f, 15f)
        )

        val dataSet = BarDataSet(entries, "Expenses")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
        barChart.data = BarData(dataSet)

        barChart.description.text = "Bar Chart"

        barChart.animateY(1000, Easing.EaseOutBounce)

        barChart.invalidate()
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