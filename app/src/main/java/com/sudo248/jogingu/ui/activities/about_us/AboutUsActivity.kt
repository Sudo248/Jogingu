package com.sudo248.jogingu.ui.activities.about_us

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar
import com.sudo248.jogingu.R
import timber.log.Timber

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
    }

    override fun onStart() {
        super.onStart()
        findViewById<MaterialToolbar>(R.id.toolbar_about).apply {
            setNavigationOnClickListener {
                Timber.d("On Navigation Click")
                this@AboutUsActivity.onBackPressed()
            }
        }
    }
}