@file:Suppress("NAME_SHADOWING")

package com.foundie.id.ui.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.support.annotation.StringRes
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.foundie.id.ui.catalog.CatalogPagerAdapter
import com.foundie.id.ui.community.CommunityPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay

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
    val context = LocalContext.current
    val activity = context as? AppCompatActivity

    AndroidView(
        factory = { context ->
            val view = View.inflate(context, R.layout.activity_home, null)

            activity?.supportActionBar?.title = context.getString(R.string.hi_gorgeous)

            val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
            val list = ArrayList<ImageDataResponse>()
            val adapter = ImageSliderAdapter(list)

            list.add(ImageDataResponse("https://i.pinimg.com/564x/bb/ad/bd/bbadbd23bef414504a195612e289a407.jpg"))
            list.add(ImageDataResponse("https://i.pinimg.com/736x/c1/2b/64/c12b649924fe3865221f7791fbd3b6b5.jpg"))

            viewPager.adapter = adapter

            view
        },
        modifier = Modifier.fillMaxSize()
    )

    LaunchedEffect(Unit) {
        var currentPage = 0

        while (true) {
            delay(delayTimeSlider)
            val viewPager = (context as? Activity)?.findViewById<ViewPager2>(R.id.view_pager)
            if (viewPager != null) {
                if (currentPage == viewPager.adapter?.itemCount) {
                    currentPage = 0
                }
                viewPager.setCurrentItem(currentPage++, true)
            }
        }
    }
}



@Composable
fun CatalogScreen() {
    AndroidView(
        factory = { context ->
            // Ensure the context is an instance of AppCompatActivity
            val activity = context as AppCompatActivity

            // Inflate the view
            val view = View.inflate(activity, R.layout.activity_catalog, null)

            // Initialize components
            val catalogPagerAdapter = CatalogPagerAdapter(activity)
            val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
            val tabs: TabLayout = view.findViewById(R.id.tabs)
            viewPager.adapter = catalogPagerAdapter

            // Set up TabLayoutMediator
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = activity.resources.getString(TAB_TITLES_CATALOG[position])
            }.attach()

            // Set action bar elevation
            activity.supportActionBar?.elevation = 0f

            view
        },
        modifier = Modifier.fillMaxSize()
    )
}

@StringRes
private val TAB_TITLES_CATALOG = intArrayOf(
    R.string.all,
    R.string.popular
)

@Composable
fun CommunityScreen() {
    AndroidView(
        factory = { context ->
            // Ensure the context is an instance of AppCompatActivity
            val activity = context as AppCompatActivity

            // Inflate the view
            val view = View.inflate(activity, R.layout.activity_community, null)

            // Initialize components
            val communityPagerAdapter = CommunityPagerAdapter(activity)
            val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
            val tabs: TabLayout = view.findViewById(R.id.tabs)
            viewPager.adapter = communityPagerAdapter

            // Set up TabLayoutMediator
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = activity.resources.getString(TAB_TITLES_COMMUNITY[position])
            }.attach()

            // Set action bar elevation
            activity.supportActionBar?.elevation = 0f

            view
        },
        modifier = Modifier.fillMaxSize()
    )
}

@StringRes
private val TAB_TITLES_COMMUNITY = intArrayOf(
    R.string.all,
    R.string.joined,
    R.string.popular
)


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