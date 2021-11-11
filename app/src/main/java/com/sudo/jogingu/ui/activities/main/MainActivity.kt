
package com.sudo.jogingu.ui.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.ActivityMainBinding
import com.sudo.jogingu.ui.activities.run.RunActivity
import com.sudo.jogingu.ui.fragments.target.TargetFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val idFragment= intent.getIntExtra("check", 0 )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)
        if (idFragment==1) {
            navHostFragment.findNavController().navigate(R.id.action_homeFragment_to_targetFragment)
        }
        binding.fabRun.setOnClickListener {
            moveToRunningActivity()
        }

    }


    private fun moveToRunningActivity(){
        val intent = Intent(this, RunActivity::class.java)
        startActivity(intent)
    }

}