package com.foundie.id.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.R
import com.foundie.id.databinding.FragmentProfileBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.loadImageWithCacheBusting
import com.foundie.id.ui.home.color_analysis.result.ColorAnalysisHistoryFragment
import com.foundie.id.ui.home.makeup_analysis.result.RMakeUpFragment
import com.foundie.id.ui.home.makeup_analysis.result.SkinToneFragment
import com.foundie.id.ui.login.dataStore
import com.foundie.id.ui.profile.profile_edit.ProfileEditFragment
import com.foundie.id.ui.profile.settings.SettingFragment
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.ProfileViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(
            this,
            ProfileViewModelFactory(requireContext())
        )[ProfileViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.Profile)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        btnClick()

        val profilePagerAdapter = ProfilePagerAdapter(requireActivity())
        binding.viewPager.adapter = profilePagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES_PROFILE[position])
        }.attach()

        (activity as? AppCompatActivity)?.supportActionBar?.elevation = 0f

        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            viewModel.getBiodata(token)
        }

        viewModel.isLoadingProfile.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.biodata.observe(viewLifecycleOwner) { biodata ->
            if (biodata != null) {
                binding.apply {
                    tvUsername.text = biodata.name
                    tvLocation.text = biodata.location
                    tvGender.text = biodata.gender
                    tvDescriptionProfile.text = biodata.description
                    tvFollowed.text = "Following: ${biodata.following}"
                    tvFollowers.text = "Followers: ${biodata.followers}"
                    ivUser.loadImageWithCacheBusting(biodata.profileImageUrl)
                    ivBackgroundUser.loadImageWithCacheBusting(biodata.coverImageUrl)
                }
            }
        }

        viewModel.profileStatus.observe(viewLifecycleOwner) { profileStatus ->
            if (profileStatus.isNullOrEmpty()) return@observe

            val isError = viewModel.isErrorProfile
            val message = if (isError && profileStatus == "Unauthorized") {
                getString(R.string.ERROR_UNAUTHORIZED)
            } else {
                profileStatus
            }
            Log.d("ProfileFragment", message)
        }

        binding.ivEditProfile.setOnClickListener {
            replaceFragment(ProfileEditFragment())
        }
    }

    private fun btnClick() {
        binding.apply {
            tvMakeupAnalysis.setOnClickListener {
                replaceFragment(RMakeUpFragment())
            }
            tvSkintoneAnalysis.setOnClickListener {
                replaceFragment(SkinToneFragment())
            }
            tvColorAnalysis.setOnClickListener {
                replaceFragment(ColorAnalysisHistoryFragment())
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                replaceFragment(SettingFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES_PROFILE = intArrayOf(
            R.string.post,
            R.string.review,
        )
    }
}
