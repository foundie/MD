package com.foundie.id.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
    Text(text = "Home Screen")
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
    Text(text = "Notifications Screen")
}

@Composable
fun ProfileScreen() {
    Text(text = "Profile Screen")
}
