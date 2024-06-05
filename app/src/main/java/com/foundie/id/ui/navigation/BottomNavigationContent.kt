package com.foundie.id.ui.navigation

import android.annotation.SuppressLint
import android.os.Handler
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.viewpager2.widget.ViewPager2
import com.foundie.id.R
import com.foundie.id.data.adapter.ImageSliderAdapter
import com.foundie.id.data.local.response.ImageDataResponse
import com.foundie.id.settings.delayTimeSlider

// Fungsi composable utama yang menyusun keseluruhan konten utama aplikasi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            // Memanggil fungsi BottomNavBar untuk membuat bottom navigation bar
            BottomNavBar(
                navHostController = navController,
                items = BottomNavigationItem.getMenuBottomItems()
            )
        },
        content = {
            NavigationHost(navController)
        }
    )
}

// Fungsi composable untuk mengatur NavHost yang mengelola navigasi antar composable
@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") { HomeScreen() }
        composable("catalog") { CatalogScreen() }
        composable("community") { CommunityScreen() }
        composable("notification") { NotificationScreen() }
        composable("profile") { ProfileScreen() }
    }
}

@Composable
fun HomeScreen() {
    AndroidView(
        factory = { context ->
            val view = View.inflate(context, R.layout.activity_home, null)

            val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
            val handler = Handler()
            val list = ArrayList<ImageDataResponse>()
            val adapter = ImageSliderAdapter(list)

            list.add(ImageDataResponse("https://i.pinimg.com/564x/bb/ad/bd/bbadbd23bef414504a195612e289a407.jpg"))
            list.add(ImageDataResponse("https://i.pinimg.com/736x/c1/2b/64/c12b649924fe3865221f7791fbd3b6b5.jpg"))

            viewPager.adapter = adapter

            val runnable = object : Runnable {
                var currentPage = 0
                override fun run() {
                    if (currentPage == list.size) {
                        currentPage = 0
                    }
                    viewPager.setCurrentItem(currentPage++, true)
                    handler.postDelayed(this, delayTimeSlider) // slideInterval
                }
            }

            handler.postDelayed(runnable, delayTimeSlider) // slideInterval

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    runnable.currentPage = position
                }
            })

            view
        },
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun CatalogScreen() {
    Text(text = "Catalog Screen")
}

@Composable
fun CommunityScreen() {
    Text(text = "Community Screen")
}

@Composable
fun NotificationScreen() {
    AndroidView(
        factory = { View.inflate(it, R.layout.activity_notification, null)
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ProfileScreen() {
    Text(text = "Profile Screen")
}