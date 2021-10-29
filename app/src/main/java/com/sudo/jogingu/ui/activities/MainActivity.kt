package com.sudo.jogingu.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.ActivityMainBinding
import com.sudo.jogingu.service.StepService
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

        binding.fab.setOnClickListener{
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }
    }

}