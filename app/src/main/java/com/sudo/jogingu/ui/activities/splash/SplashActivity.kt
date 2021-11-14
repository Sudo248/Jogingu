package com.sudo.jogingu.ui.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.sudo.domain.use_case.app.IsFirstOpenApp
import com.sudo.jogingu.R
import com.sudo.jogingu.common.Constant.ACTION_SETUP_EVERYDAY_NOTIFICATION
import com.sudo.jogingu.common.Constant.OPEN_FRAGMENT
import com.sudo.jogingu.service.NotificationService
import com.sudo.jogingu.ui.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var isFirstOpenAppUseCase: IsFirstOpenApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launchWhenStarted {
            Timber.d("is first open app: ${isFirstOpenAppUseCase()}")
            val idFragment = if(isFirstOpenAppUseCase()){
                isFirstOpenAppUseCase(false)
                sendCommandToSetupEverydayNotification()
                3
            }else{
                0
            }
            delay(1000)
            moveToMainActivity(idFragment)
        }
    }

    private fun moveToMainActivity(idFragment: Int){
        Intent(this, MainActivity::class.java).also {
            it.putExtra(OPEN_FRAGMENT, 3)
            this.startActivity(it)
        }
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun sendCommandToSetupEverydayNotification(){
        Intent(this, NotificationService::class.java).also {
            it.action = ACTION_SETUP_EVERYDAY_NOTIFICATION
            this.startService(it)
        }
    }

}