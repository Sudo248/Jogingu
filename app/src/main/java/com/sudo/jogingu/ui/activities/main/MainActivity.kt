package com.sudo.jogingu.ui.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sudo.jogingu.R
import com.sudo.jogingu.common.Constant.OPEN_FRAGMENT
import com.sudo.jogingu.databinding.ActivityMainBinding
import com.sudo.jogingu.ui.activities.about_us.AboutUsActivity
import com.sudo.jogingu.ui.activities.run.RunActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        var idFragment = 0
        savedInstanceState?.let{
            idFragment = it.getInt(OPEN_FRAGMENT)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

//        navigateToFragment(idFragment)

        binding.fabRun.setOnClickListener {
            moveToRunningActivity()
        }

        binding.logo.setOnClickListener {
            this.startActivity(Intent(this, AboutUsActivity::class.java))
        }

    }


    private fun navigateToFragment(id: Int){
        when(id){
            1 -> {
                // navigate to target Fragment
                findNavController(R.id.bottom_navigation).navigate(R.id.action_homeFragment_to_targetFragment)
            }
            2 -> {
                // navigate to statistic fragment
                findNavController(R.id.bottom_navigation).navigate(R.id.action_homeFragment_to_statisticFragment)
            }
            3 -> {
                // navigate to Profile Fragment
                findNavController(R.id.bottom_navigation).navigate(R.id.action_homeFragment_to_profileFragment)
            }
            else -> Unit
        }
    }

    private fun moveToRunningActivity(){
        val intent = Intent(this, RunActivity::class.java)
        startActivity(intent)
    }

}