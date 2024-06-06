package com.foundie.id.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.MainActivity
import com.foundie.id.R
import com.foundie.id.ThemeActivity
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.delayTime
import com.foundie.id.ui.home.HomeActivity
import com.foundie.id.ui.login.LoginActivity
import com.foundie.id.ui.login.dataStore
import com.foundie.id.ui.navigation.HomeScreen

@SuppressLint("CustomSplashScreen")
open class SplashScreenActivity : ThemeActivity() {
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

        val prefen = SettingsPreferences.getInstance(dataStore)
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]

        authViewModel.getUserLoginSession().observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                // Menambahkan delay selama 1 detik sebelum berpindah ke HomeActivity
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }, delayTime)
            } else {
                // Menambahkan delay selama 1 detik sebelum berpindah ke LoginActivity
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }, delayTime)
            }
        }
    }
}