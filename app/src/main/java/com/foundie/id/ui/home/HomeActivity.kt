package com.foundie.id.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foundie.id.R
import com.foundie.id.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title =  getString(R.string.hi_gorgeous)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}