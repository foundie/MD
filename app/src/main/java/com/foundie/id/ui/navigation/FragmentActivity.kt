package com.foundie.id.ui.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.foundie.id.R
import com.foundie.id.databinding.ActivityFragmentBinding
import com.foundie.id.ui.catalog.CatalogFragment
import com.foundie.id.ui.community.CommunityFragment
import com.foundie.id.ui.home.HomeFragment
import com.foundie.id.ui.notification.NotificationFragment
import com.foundie.id.ui.profile.ProfileFragment

@Suppress("DEPRECATION")
class FragmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentBinding
    private var currentFragmentId: Int = R.id.home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    currentFragmentId = R.id.home
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.catalog -> {
                    currentFragmentId = R.id.catalog
                    replaceFragment(CatalogFragment())
                    true
                }
                R.id.community -> {
                    currentFragmentId = R.id.community
                    replaceFragment(CommunityFragment())
                    true
                }
                R.id.notification -> {
                    currentFragmentId = R.id.notification
                    replaceFragment(NotificationFragment())
                    true
                }
                R.id.profile -> {
                    menuItem.title = "Profile"
                    currentFragmentId = R.id.profile
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getInt("currentFragmentId", R.id.home)
        }

        when (currentFragmentId) {
            R.id.home -> replaceFragment(HomeFragment())
            R.id.catalog -> replaceFragment(CatalogFragment())
            R.id.community -> replaceFragment(CommunityFragment())
            R.id.notification -> replaceFragment(NotificationFragment())
            R.id.profile -> replaceFragment(ProfileFragment())

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentFragmentId", currentFragmentId)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}