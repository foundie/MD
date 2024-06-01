package com.foundie.id.ui.navigation

import com.foundie.id.R

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val route: String,
) {

    companion object {
        fun getMenuBottomItems() = mutableListOf(
              BottomNavigationItem(
                title = "Home",
                selectedIcon = R.drawable.ic_button_home,
                unselectedIcon = R.drawable.ic_button_home,
                route = "home"
            ),
            BottomNavigationItem(
                title = "Catalog",
                selectedIcon = R.drawable.ic_button_catalog,
                unselectedIcon = R.drawable.ic_button_catalog,
                route = "catalog"
            ),
            BottomNavigationItem(
                title = "Community",
                selectedIcon = R.drawable.ic_button_community,
                unselectedIcon = R.drawable.ic_button_community,
                route = "community"
            ),
            BottomNavigationItem(
                title = "Notification",
                selectedIcon = R.drawable.ic_button_notification,
                unselectedIcon = R.drawable.ic_button_notification,
                route = "notification"
            ),
            BottomNavigationItem(
                title = "Profile",
                selectedIcon = R.drawable.ic_button_account,
                unselectedIcon = R.drawable.ic_button_account,
                route = "profile"
            )
        )
    }
}
