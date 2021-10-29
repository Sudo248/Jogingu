package com.sudo.jogingu.ui.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.ActivityMainBinding
import com.sudo.jogingu.ui.activities.run.RunningActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

        binding.fabRun.setOnClickListener {
            moveToRunningActivity()
        }

    }

    private fun moveToRunningActivity(){
        val intent = Intent(this, RunningActivity::class.java)
        startActivity(intent)
    }

}