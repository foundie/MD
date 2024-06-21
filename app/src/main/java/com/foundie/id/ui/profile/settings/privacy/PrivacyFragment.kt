package com.foundie.id.ui.profile.settings.privacy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foundie.id.R
import com.foundie.id.databinding.ActivityPrivacyBinding

@Suppress("DEPRECATION")
class PrivacyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.privacy_policy)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
