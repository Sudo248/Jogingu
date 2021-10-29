package com.sudo.jogingu.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sudo.jogingu.R
import com.sudo.jogingu.callback.StepCallback
import com.sudo.jogingu.databinding.ActivityRunningBinding
import com.sudo.jogingu.helper.GeneralHelper
import com.sudo.jogingu.service.StepService
import timber.log.Timber

class RunningActivity : AppCompatActivity(), StepCallback {
    lateinit var binding: ActivityRunningBinding
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.tag("Vao RunningActivity roi")

        binding.btnStartCount.setOnClickListener{
            binding.tvStepCount.text = "0"
            val intent = Intent(this, StepService::class.java)
            startService(intent)
            StepService.Subscribe.register(this)

            val sharedPreferences = getSharedPreferences("myPre", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("primaryKey", 0)
            editor.apply()
        }

    }

    override fun subscribeSteps(steps: Int) {
        binding.tvStepCount.text = steps.toString()
//        TV_CALORIES.setText(GeneralHelper.getCalories(steps))
//        TV_DISTANCE.setText(GeneralHelper.getDistanceCovered(steps))
    }
}