package com.foundie.id.ui

import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.foundie.id.R
import com.foundie.id.data.image_slider.ImageData
import com.foundie.id.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var adapter: ImageSliderAdapter
    private val list = ArrayList<ImageData>()
    private lateinit var dots: ArrayList<TextView>
    private var currentPage = 0
    private val slideInterval = 1000L // Durasi antara perpindahan slide (dalam milidetik)
    private val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gambar slider
        list.add(
            ImageData(
                "https://i.pinimg.com/564x/fb/49/ea/fb49ea99fd92046c37f9f0fb192ce784.jpg"
            )
        )
        list.add(
            ImageData(
                "https://i.pinimg.com/564x/38/16/18/3816180c64273eeb2c75fce39d98cfb9.jpg"
            )
        )

        adapter = ImageSliderAdapter(list)
        binding.viewPager.adapter = adapter
        dots = ArrayList()
        setIndicator()

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
                selectedDot(position)
                currentPage = position
                super.onPageSelected(position)
            }
        })
    }

    // Fungsi untuk mengatur posisi dots slider berdasarkan gambarnya
    private fun selectedDot(position: Int) {
        val selectedColor = if (isDarkTheme()) ContextCompat.getColor(this, R.color.primayColorDark)
        else ContextCompat.getColor(this, R.color.primaryColor)

        for (i in 0 until list.size) {
            dots[i].setTextColor(if (i == position) selectedColor else secondaryTextColor())
        }
    }

    private fun isDarkTheme(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    // Mengatur warna dots
    private fun secondaryTextColor(): Int {
        return if (isDarkTheme()) ContextCompat.getColor(
            this,
            androidx.appcompat.R.color.secondary_text_default_material_dark
        )
        else ContextCompat.getColor(
            this,
            androidx.appcompat.R.color.secondary_text_default_material_light
        )
    }

    // Fungsi untuk mengatur dots-nya
    private fun setIndicator() {
        for (i in 0 until list.size) {
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("\u25CF", Html.FROM_HTML_MODE_LEGACY).toString()

            dots[i].textSize = 18f
            binding.dotsIndicator.addView(dots[i])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan pemutaran slide otomatis saat aktivitas dihancurkan
        handler.removeCallbacks(runnable)
    }
}
