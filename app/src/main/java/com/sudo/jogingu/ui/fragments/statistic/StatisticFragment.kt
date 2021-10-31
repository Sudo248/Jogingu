package com.sudo.jogingu.ui.fragments.statistic

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sudo.data.local.database.dao.JoginguDao
import com.sudo.data.repositories.MainRepositoryImpl
import com.sudo.domain.common.Result
import com.sudo.domain.entities.Run
import com.sudo.domain.entities.RunningDay
import com.sudo.domain.use_case.run.AddNewRun
import com.sudo.domain.use_case.run.GetAllRuns
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentStatisticBinding
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.ArrayList


class StatisticFragment : Fragment() {
    lateinit var binding: FragmentStatisticBinding
    lateinit var newRun: AddNewRun
    lateinit var getAllRun : MainRepositoryImpl
    lateinit var runningList : Flow<Result<List<Run>>>
    private var runList = ArrayList<RunningDay>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticBinding.inflate(inflater,container,false)

        binding.statisticBtnDay.setOnClickListener{
            runList.clear()
            runList = getFakeRunList(24)
            setUpBarChart()
        }
        binding.statisticBtnWeek.setOnClickListener{
            runList.clear()
            runList = getFakeRunList(7)
            setUpBarChart()
        }
        binding.statisticBtnMonth.setOnClickListener{
            runList.clear()
            runList = getFakeRunList(31)
            setUpBarChart()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runList = getFakeRunList(7)
        setUpBarChart()
    }

    //up chart
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpBarChart(){


        initBarChart()
    // draw bar chart with dynamic data

        val entries: ArrayList<BarEntry> = ArrayList()

    // Can replace thí data object with the custom object
        for( i in runList.indices){
            val runningday = runList[i]
            entries.add(BarEntry(i.toFloat(),runningday.distance.toFloat(),"${i%4}"))
        }
    // set show data distance
        val barDataSet = BarDataSet(entries,"Distance")


    // set color
        barDataSet.setColors(resources.getColor(R.color.main_color_normal, null))
        barDataSet.setDrawValues(false)
        val data = BarData(barDataSet)
    // add data to chart
        binding.barChart.data = data

        binding.barChart.invalidate()

    }

    @SuppressLint("ResourceAsColor")
    private fun initBarChart() {
    // hide grid lines
        binding.barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = binding.barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

    // remove right y-axis
        binding.barChart.axisRight.isEnabled = false

    // remove legend
        binding.barChart.legend.isEnabled = false

    // set background color
        //binding.barChart.setBackgroundColor(R.color.main_color_light)

    // remove description label
        binding.barChart.description.isEnabled = false

    // add animation
        binding.barChart.animateY(1000)

    // draw label on x Axis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.valueFormatter = MyAxisFormatter()

        xAxis.setDrawLabels(true)

        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +0f
    }

    // set format for column
    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d(ContentValues.TAG, "getAxisLabel: index $index")
            return if (index < runList.size) {
                runList[index].day
            } else {
                ""
            }
        }
    }
    //----------------------------------- get list distance user run in a week Fake data-----------------------------------
    private  fun getFakeRunList(len: Int): ArrayList<RunningDay> {
//        newRun.invoke(Run("R1","evening run",1200,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,10,31,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",1400,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,1,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",1300,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,2,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",100,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,3,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",1900,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,4,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",3000,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,5,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",4000,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,6,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",500,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,7,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",1200,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,8,21,14,12),"Ha Noi"))
//
//        newRun.invoke(Run("R1","evening run",1700,7f,132,"http://i.imgur.com/DvpvklR.png",123,
//            Date(2021,11,9,21,14,12),"Ha Noi"))
//        runningList = getAllRun.getAllRuns()
//        runningList.collect{}
        for(i in 1..len){
            if(len==7)
                runList.add(RunningDay("T${i}", i%3))
            else{
                runList.add(RunningDay("${i}", i%7))
            }
        }
        return runList
    }


}