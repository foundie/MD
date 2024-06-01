package com.foundie.id.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navHostController: NavHostController,
    items: List<BottomNavigationItem>
) {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.onSurface

    NavigationBar {
        items.forEachIndexed { index, bottomNavigationItem ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == bottomNavigationItem.route } == true,
                onClick = {
                    selectedIndex = index
                    navHostController.navigate(bottomNavigationItem.route) {
                        navHostController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(if (index == selectedIndex) bottomNavigationItem.selectedIcon else bottomNavigationItem.unselectedIcon),
                        contentDescription = bottomNavigationItem.title,
                        tint = if (index == selectedIndex) selectedColor else unselectedColor // Warna ikon
                    )
                }
                ,
                label = {
                    Text(
                        text = bottomNavigationItem.title,
                        color = if (index == selectedIndex) selectedColor else unselectedColor, // Warna teks
                        fontSize = 9.sp
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}
