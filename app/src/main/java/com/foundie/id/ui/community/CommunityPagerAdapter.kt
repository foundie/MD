package com.foundie.id.ui.community

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = CommunityAllFragment()
            1 -> fragment = CommunityJoinedFragment()
            2 -> fragment = CommunityPopularFragment()
        }
        return fragment as Fragment
    }
}