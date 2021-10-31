package com.sudo.jogingu

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import com.sudo.jogingu.service.TestJobService


class MainActivity : AppCompatActivity() {
    private val notificationManagerCompat: NotificationManagerCompat? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        creNotiSever()
        setContentView(R.layout.activity_main)

    }
    fun creNotiSever() {
        var componentName = ComponentName(this, TestJobService::class.java)
        val JOB_ID : Int =123
        var jobInfo = JobInfo.Builder(JOB_ID,componentName )
            .setPersisted(true)
            .setPeriodic(24*60*60*1000)
            .build()
        var jobScheduler : JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)


    }
}