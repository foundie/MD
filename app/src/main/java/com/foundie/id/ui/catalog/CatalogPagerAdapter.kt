package com.foundie.id.ui.catalog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CatalogPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CatalogAllFragment()
            1 -> CatalogPopularFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
