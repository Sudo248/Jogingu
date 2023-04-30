package com.sudo248.jogingu.ui.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sudo248.jogingu.R
import com.sudo248.jogingu.ui.activities.auth.AuthActivity
import com.sudo248.jogingu.ui.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(1500)
                if (viewModel.checkUserLogin().await()) {
                    moveToMainActivity()
                } else {
                    moveToAuthActivity()
                }
            }
        }
    }

    private fun moveToMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    private fun moveToAuthActivity() {
        startActivity(Intent(this, AuthActivity::class.java))
        this.finish()
    }

}