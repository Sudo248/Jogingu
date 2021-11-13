package com.sudo.jogingu.ui.fragments.statistic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sudo.domain.common.Result
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.RunningDay
import com.sudo.domain.use_case.run.AddNewRun
import com.sudo.domain.use_case.run.GetAllRuns
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentStatisticBinding
import com.sudo.jogingu.ui.fragments.statistic.viewmodels.StatisticViewModel

import kotlinx.coroutines.flow.*
import java.util.*

import kotlin.collections.ArrayList


class StatisticFragment : Fragment() {
    lateinit var binding: FragmentStatisticBinding
//    lateinit var newRun: AddNewRun
//    lateinit var getAllRun : GetAllRuns
//    lateinit var runningList : Flow<Result<List<Run>>>
    private var runList = ArrayList<RunningDay>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        var viewModel: StatisticViewModel? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater,container,false)
        setupViewModel()
        binding.statisticBtnDay.setOnClickListener{
            runList.clear()
            runList = viewModel?.getDataStatisticFilter(45) as ArrayList<RunningDay>
            setUpBarChart()
            updateView(45)
        }
        binding.statisticBtnWeek.setOnClickListener{
            runList.clear()
            runList = viewModel?.getDataStatisticFilter(8) as ArrayList<RunningDay>
            setUpBarChart()
            updateView(7)
        }
        binding.statisticBtnMonth.setOnClickListener{
            runList.clear()
            runList = viewModel?.getDataStatisticFilter(31) as ArrayList<RunningDay>
            setUpBarChart()
            updateView(31)
        }

        return binding.root
    }

    private fun updateView(time: Int) {
        binding.statisticDate.text = viewModel!!.getTime(time)
        binding.statisticTvSteps.text = viewModel!!.getSteps().toString()
        binding.statisticTvCalories.text = viewModel!!.getCalories().toString()
        binding.statisticTvTime.text = viewModel!!.getTime().toString()
        binding.statisticTvKm.text = viewModel!!.getDistance().toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runList = viewModel?.getDataStatisticFilter(7) as ArrayList<RunningDay>
        setUpBarChart()
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(StatisticViewModel::class.java)

    }
    // set up chart
    private fun setUpBarChart(){


        initBarChart()
    //draw bar chart with dynamic data

        val entries: ArrayList<BarEntry> = ArrayList()

    // Can replace thí data object with the custom object
        for( i in runList.indices){
            val runningday = runList[i]
            entries.add(BarEntry(i.toFloat(),runningday.distance.toFloat()))

        }


    //set show data distance
        val barDataSet = BarDataSet(entries,"")

    // set color
        barDataSet.setColors(getResources().getColor(R.color.main_color_light))
        barDataSet.setDrawValues(false)

        val data = BarData(barDataSet)
    //add data to chart
        binding.barChart.data = data

        binding.barChart.invalidate()

    }

    @SuppressLint("ResourceAsColor")
    private fun initBarChart() {
    //hide grid lines
        binding.barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = binding.barChart.xAxis
        xAxis.setDrawGridLines(false)

    //remove right y-axis
        binding.barChart.axisRight.isEnabled = false

    //remove legend
        binding.barChart.legend.isEnabled = false

    //set background color
        //binding.barChart.setBackgroundColor(R.color.main_color_light)

    //remove description label
        binding.barChart.description.isEnabled = false
        binding.barChart.xAxis.setLabelCount(7,true)
    //add animation
        binding.barChart.animateY(1000)
        binding.barChart.setExtraOffsets ( 1f, 1f, 1f , 1f )
    //draw label on x Axis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.valueFormatter= viewModel!!.MyAxisFormatter()
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +0f
    }



}