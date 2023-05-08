package com.sudo248.jogingu.ui.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sudo248.data.shared_preference.SharedPref
import com.sudo248.jogingu.R
import com.sudo248.jogingu.common.Constant.OPEN_FRAGMENT
import com.sudo248.jogingu.databinding.ActivityMainBinding
import com.sudo248.jogingu.ui.activities.about_us.AboutUsActivity
import com.sudo248.jogingu.ui.activities.run.RunActivity
import com.sudo248.jogingu.util.DialogUtils
import com.sudo248.jogingu.util.gone
import com.sudo248.jogingu.util.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var pref: SharedPref

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
            if (pref.isSetUserInfo()) {
                moveToRunningActivity()
            } else {
                DialogUtils.showDialog(
                    this,
                    title = "Required",
                    description = "Please set user info before running"
                )
            }

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

    fun hideBottomNavigation() {
        binding.bottomAppbar.gone()
        binding.fabRun.gone()
    }

    fun showBottomNavigation() {
        binding.bottomAppbar.visible()
        binding.fabRun.visible()
    }

}