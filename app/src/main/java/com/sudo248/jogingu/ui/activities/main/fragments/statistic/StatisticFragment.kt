package com.sudo248.jogingu.ui.activities.main.fragments.statistic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sudo248.domain.entities.RunInStatistic
import com.sudo248.jogingu.R
import com.sudo248.jogingu.databinding.FragmentStatisticBinding
import dagger.hilt.android.AndroidEntryPoint

import kotlin.collections.ArrayList

@AndroidEntryPoint
class StatisticFragment : Fragment() {
    lateinit var binding: FragmentStatisticBinding

    private val viewModel: StatisticViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.statistic)
        initBarChart()
        subscribeUi()
        observer()
    }

    private fun subscribeUi(){
        binding.statisticBtnDay.setOnClickListener{
            binding.statisticDate.text = getString(R.string.today)
            viewModel.getRunsThisDay()
        }
        binding.statisticBtnWeek.setOnClickListener{
            binding.statisticDate.text = getString(R.string.this_week)
            viewModel.getRunsThisWeek()
        }
        binding.statisticBtnMonth.setOnClickListener{
            binding.statisticDate.text = getString(R.string.this_month)
            viewModel.getRunsThisMonth()
        }
    }

    private fun observer(){
        viewModel.listRun.observe(viewLifecycleOwner){
            setUpBarChart(it)
            updateView(
                viewModel.totalStep,
                viewModel.totalCaloBurned,
                viewModel.totalTimeRunning,
                viewModel.totalDistance
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateView(
        totalStep: Int,
        totalCaloBurned: Float,
        totalTimeRunning: Int,
        totalDistance: Float
    ) {
        binding.statisticTvSteps.text = totalStep.toString()
        binding.statisticTvCalories.text = "%.2f".format(totalCaloBurned/10000000f)
        binding.statisticTvTime.text = totalTimeRunning.toString()
        binding.statisticTvKm.text = "%.2f".format(totalDistance/1000f)
    }

    // set up chart
    private fun setUpBarChart(listRun: List<RunInStatistic?>){

        //draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        // Can replace thí data object with the custom object
        for( i in listRun.indices){
            val runningDay = listRun[i]
            entries.add(BarEntry(i.toFloat(), runningDay?.distance ?: 0.0f))
        }
        //set show data distance
        val barDataSet = BarDataSet(entries,"Distance")

        // set color
        barDataSet.setColors(getColor(requireContext(), R.color.main_color_light))
        barDataSet.setDrawValues(false)

//      add data to chart
        binding.barChart.data = BarData(barDataSet)

        binding.barChart.xAxis.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                return if(listRun.size <= 7){
                    if(index == 0) "cn"
                    else "${index+1}"
                }else{
                    "${index+1}"
                }
            }
        }
        //add animation
        binding.barChart.animateY(1000)

        binding.barChart.invalidate()
    }

    @SuppressLint("ResourceAsColor")
    private fun initBarChart() {
        //hide grid lines
        binding.barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = binding.barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        binding.barChart.axisRight.isEnabled = false

        //remove legend
        binding.barChart.legend.isEnabled = false

        //remove description label
        binding.barChart.description.isEnabled = false

        binding.barChart.xAxis.setLabelCount(7,true)

        //draw label on x Axis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +0f
    }


}