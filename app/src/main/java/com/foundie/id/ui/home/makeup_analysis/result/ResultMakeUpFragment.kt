package com.foundie.id.ui.home.makeup_analysis.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.foundie.id.R
import com.foundie.id.databinding.FragmentResultMakeUpBinding
import com.google.android.material.tabs.TabLayoutMediator

class ResultMakeUpFragment : Fragment() {

    private var _binding: FragmentResultMakeUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultMakeUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultMakeUpAdapter = ResultMakeUpAdapter(requireActivity())
        binding.viewPager.adapter = resultMakeUpAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES_COMMUNITY[position])
        }.attach()

        (activity as? AppCompatActivity)?.supportActionBar?.elevation = 0f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES_COMMUNITY = intArrayOf(
            R.string.makeup_result,
            R.string.skin_tone_result,
        )
    }
}