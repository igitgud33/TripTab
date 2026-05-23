package edu.cit.boquia.triptab.screens.summary

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.boquia.triptab.R
import edu.cit.boquia.triptab.app.CustomApp
import edu.cit.boquia.triptab.screens.main.MainActivity
import edu.cit.boquia.triptab.screens.plan.Plan
import edu.cit.boquia.triptab.screens.plan.PlanModel
import edu.cit.boquia.triptab.screens.profile.ProfileActivity
import edu.cit.boquia.triptab.utils.setupBottomNavigation
import edu.cit.boquia.triptab.utils.toCurrency

class SummaryActivity : AppCompatActivity(), SummaryContract.View{
    private lateinit var presenter: SummaryContract.Presenter

    // pie chart var
    lateinit var pieChart: PieChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary)

        val planModel = PlanModel(application as CustomApp)
        presenter = SummaryPresenter(this, planModel)
        presenter.loadSummary()

        setupBottomNavigation(R.id.bottom_nav_summary, R.id.nav_summary)

    }

    override fun displaySummary(plans: List<Plan>, totalExpenses: Double) {
        val container = findViewById<LinearLayout>(R.id.containerDynamicPlans)
        val tvEmpty = findViewById<TextView>(R.id.tvNoPlansForSummary)

        container.removeAllViews()
        tvEmpty.visibility = android.view.View.GONE

        // prepare pie chart data
        val entries: ArrayList<PieEntry> = ArrayList()
        val colors: ArrayList<Int> = ArrayList()

        // color palette
        val colorPalette = listOf(
            resources.getColor(R.color.red),
            resources.getColor(R.color.yellow),
            resources.getColor(R.color.teal_200),
            Color.GREEN, Color.BLUE, Color.GRAY
        )

        // loop through plans for both chart & list
        plans.forEachIndexed { index, plan ->
            val planTotal = plan.expenses.sumOf { it.amount }

            if(planTotal > 0) {
                val color = colorPalette[index % colorPalette.size]

                // add to pie chart
                entries.add(PieEntry(planTotal.toFloat(), plan.name))
                colors.add(color)

                // add dropdown dynamic list
                val itemView = layoutInflater.inflate(R.layout.item_summary_plan, container, false)

                // set plan color, name, value, & %
                itemView.findViewById<android.view.View>(R.id.vPlanColor).setBackgroundColor(color)
                itemView.findViewById<TextView>(R.id.tvPlanName).text = plan.name
                itemView.findViewById<TextView>(R.id.tvPlanValue).text = planTotal.toCurrency

                val percent = if(totalExpenses > 0) (planTotal / totalExpenses * 100) else 0.0
                itemView.findViewById<TextView>(R.id.tvPlanPercent).text = percent.toCurrency

                container.addView(itemView)
            }
        }



        // pie chart section
        pieChart = findViewById(R.id.summaryPieChart)
        pieChart.setUsePercentValues(true) // setting user %
        pieChart.getDescription().setEnabled(false) // remove desc
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f) // offset

        // drag
        pieChart.setDragDecelerationFrictionCoef(0.95f)

        // center hole
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        // circle color & alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // hole radius
        pieChart.setHoleRadius(60f)
        pieChart.setTransparentCircleRadius(62f)

        // center text
        pieChart.setDrawCenterText(true)

        // rotation
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        // disable legend
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)


        // set plan data set
        val dataSet = PieDataSet(entries, "")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f


        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.BLACK)

        // set all data
        pieChart.setData(data)
        pieChart.setUsePercentValues(true)
        // load chart
        pieChart.invalidate()
    }

    override fun showEmptyState() {
        val container = findViewById<LinearLayout>(R.id.containerDynamicPlans)
        val tvEmpty = findViewById<TextView>(R.id.tvNoPlansForSummary)
        val pieChart = findViewById<PieChart>(R.id.summaryPieChart)

        container.removeAllViews()
        // show the "is empty" text
        tvEmpty.visibility = android.view.View.VISIBLE
        pieChart.visibility = android.view.View.INVISIBLE
    }

}