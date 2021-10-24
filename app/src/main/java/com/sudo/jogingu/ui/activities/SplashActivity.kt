package com.sudo.jogingu.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.sudo.jogingu.R
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launchWhenStarted {
            delay(1000)
            moveToMainActivity()
        }
    }

    private fun moveToMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}