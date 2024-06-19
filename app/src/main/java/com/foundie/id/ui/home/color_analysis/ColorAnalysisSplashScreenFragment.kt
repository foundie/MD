package com.foundie.id.ui.home.color_analysis

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.foundie.id.R
import com.foundie.id.ThemeActivity

class ColorAnalysisSplashScreenFragment : ThemeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Memastikan tema aplikasi disetel sebelum layout diatur
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        setContentView(R.layout.fragment_splash_screen)

        // Menyesuaikan visibilitas gambar latar belakang berdasarkan tema saat ini
        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            findViewById<View>(R.id.backgroundImageViewLight).visibility = View.GONE
            findViewById<View>(R.id.backgroundImageViewDark).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.backgroundImageViewLight).visibility = View.VISIBLE
            findViewById<View>(R.id.backgroundImageViewDark).visibility = View.GONE
        }
    }
}