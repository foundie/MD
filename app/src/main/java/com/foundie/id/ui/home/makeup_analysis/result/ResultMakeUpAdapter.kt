package com.foundie.id.ui.home.makeup_analysis.result

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ResultMakeUpAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RMakeUpFragment()
            1 -> SkinToneFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}