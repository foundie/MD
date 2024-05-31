package com.foundie.id.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.foundie.id.R
import com.foundie.id.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Memastikan tema aplikasi disetel sebelum layout diatur
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        setContentView(R.layout.activity_splash_screen)

        // Menyesuaikan visibilitas gambar latar belakang berdasarkan tema saat ini
        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            findViewById<View>(R.id.backgroundImageViewLight).visibility = View.GONE
            findViewById<View>(R.id.backgroundImageViewDark).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.backgroundImageViewLight).visibility = View.VISIBLE
            findViewById<View>(R.id.backgroundImageViewDark).visibility = View.GONE
        }

        // Menambahkan delay selama 1 detik sebelum berpindah ke LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1000)
    }
}