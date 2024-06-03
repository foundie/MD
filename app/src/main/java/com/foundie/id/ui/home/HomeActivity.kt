package com.foundie.id.ui.home

import android.os.Bundle
import android.os.Handler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.foundie.id.R
import com.foundie.id.data.adapter.ImageSliderAdapter
import com.foundie.id.data.local.response.ImageDataResponse
import com.foundie.id.databinding.ActivityHomeBinding
import com.foundie.id.ui.navigation.MainContent
import com.foundie.id.ui.navtheme.MyTheme

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ImageSliderAdapter
    private val list = ArrayList<ImageDataResponse>()
    private val handler = Handler()
    private var currentPage = 0
    private val slideInterval = 2000L // Durasi antara perpindahan slide (dalam milidetik)
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MainContent()
            }
        }

        supportActionBar?.title =  getString(R.string.hi_gorgeous)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gambar slider
        list.add(
            ImageDataResponse(
                "https://i.pinimg.com/564x/bb/ad/bd/bbadbd23bef414504a195612e289a407.jpg"
            )
        )
        list.add(
            ImageDataResponse(
                "https://i.pinimg.com/736x/c1/2b/64/c12b649924fe3865221f7791fbd3b6b5.jpg"
            )
        )

        adapter = ImageSliderAdapter(list)
        binding.viewPager.adapter = adapter

        // Atur pemutaran slide otomatis
        runnable = Runnable {
            if (currentPage == list.size) {
                currentPage = 0
            }
            binding.viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(runnable, slideInterval)
        }

        handler.postDelayed(runnable, slideInterval)

        // Menangani perpindahan halaman
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                super.onPageSelected(position)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan pemutaran slide otomatis saat aktivitas dihancurkan
        handler.removeCallbacks(runnable)
    }
}