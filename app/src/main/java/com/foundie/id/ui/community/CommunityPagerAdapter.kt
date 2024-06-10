package com.foundie.id.ui.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CommunityAllFragment()
            1 -> CommunityJoinedFragment()
            2 -> CommunityPopularFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
