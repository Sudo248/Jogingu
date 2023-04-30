package com.sudo248.jogingu.ui.activities.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sudo248.domain.common.DataState
import com.sudo248.jogingu.R
import com.sudo248.jogingu.common.Constant
import com.sudo248.jogingu.databinding.ActivityAuthBinding
import com.sudo248.jogingu.service.NotificationService
import com.sudo248.jogingu.ui.activities.auth.fragment.LoginFragment
import com.sudo248.jogingu.ui.activities.auth.fragment.SignUpFragment
import com.sudo248.jogingu.ui.activities.main.MainActivity
import com.sudo248.jogingu.util.DialogUtils
import com.sudo248.jogingu.util.KeyboardUtils
import com.sudo248.jogingu.util.gone
import com.sudo248.jogingu.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()
    private var isSignIn = true
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListener()
        observer()
    }

    private fun setupClickListener() {
        loadingDialog = DialogUtils.loadingDialog(this)
        binding.apply {
            txtToSignUp.setOnClickListener {
                KeyboardUtils.hide(this@AuthActivity)
                navigate(1)
            }

            btnBack.setOnClickListener {
                KeyboardUtils.hide(this@AuthActivity)
                onBackPressed()
            }

            fabSignIn.setOnClickListener {
                KeyboardUtils.hide(this@AuthActivity)
                if (isSignIn) {
                    viewModel.login()
                } else {
                    viewModel.signUp()
                }
            }
        }
    }

    private fun observer() {
        viewModel.result.observe(this) {
            when(it) {
                is DataState.Loading -> {
                    loadingDialog.show()
                    binding.root.isFocusable = false
                }
                is DataState.Error ->{
                    if(loadingDialog.isShowing){
                        loadingDialog.dismiss()
                        binding.root.isFocusable = true
                    }
                }
                else ->{
                    if(loadingDialog.isShowing)
                        loadingDialog.dismiss()
                    moveToMainActivity()
                }
            }
        }
    }

    private fun moveToMainActivity() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (viewModel.isFirstOpenApp().await()) {
//                    sendCommandToSetupEverydayNotification()
                    viewModel.setIsFirstOpenApp(false).await()
                }
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                this@AuthActivity.finish()
            }
        }
    }

    private fun viewLogin() {
        binding.apply {
            lnNewUser.visible()
            btnBack.gone()
            txtTitle.text = getString(R.string.sign_in)
            isSignIn = true
        }
    }

    private fun viewSignUp() {
        binding.apply {
            lnNewUser.gone()
            btnBack.visible()
            txtTitle.text = getString(R.string.sign_up)
            isSignIn = false
        }
    }


    fun navigate(position: Int = 0) {
        when(position) {
            0 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv, LoginFragment())
                    .commit()
                viewLogin()
            }
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv, SignUpFragment())
                    .commit()
                viewSignUp()
            }
            else -> Unit
        }
    }

    private fun sendCommandToSetupEverydayNotification(){
        Intent(this, NotificationService::class.java).also {
            it.action = Constant.ACTION_SETUP_EVERYDAY_NOTIFICATION
            this.startService(it)
        }
    }

    override fun onBackPressed() {
        if(!isSignIn) {
            navigate(0)
        } else {
            super.onBackPressed()
        }
    }
}