package com.foundie.id.ui.profile.user_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.R
import com.foundie.id.data.local.response.UserDetail
import com.foundie.id.databinding.FragmentUserDetailBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.loadImageWithCacheBusting
import com.foundie.id.ui.login.dataStore
import com.foundie.id.ui.profile.ProfileViewModel
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.ProfileViewModelFactory

@Suppress("DEPRECATION")
class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
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
        _binding =  FragmentUserDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.Profile)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
        }

        viewModel.isLoadingProfile.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        val profileUser: UserDetail? = arguments?.getParcelable("USER_DATA")
        Log.d("UserDetailFragment", "Profile user: $profileUser")
        profileUser?.let {
            viewModel.getDetailUser(token, it.email)
    }

        viewModel.detailUser.observe(viewLifecycleOwner) { detailUser ->
            if (detailUser != null) {
                binding.apply {
                    tvUsername.text = detailUser.name
                    tvLocation.text = detailUser.location
                    tvGender.text = detailUser.gender
                    tvDescriptionProfile.text = detailUser.description
//                    tvFollowed.text = "Following: ${detailUserfollowersCount}"
//                    tvFollowers.text = "Followers: ${detailUser.follo}"
                    ivUser.loadImageWithCacheBusting(detailUser.profilePictureUrl)
                    ivBackgroundUser.loadImageWithCacheBusting(detailUser.coverPictureUrl)
                }
            }
        }

        viewModel.detailStatus.observe(viewLifecycleOwner) { profileDetailStatus ->
            if (profileDetailStatus.isNullOrEmpty()) return@observe

            val isError = viewModel.isErrorDetail
            val message = if (isError && profileDetailStatus == "Unauthorized") {
                getString(R.string.ERROR_UNAUTHORIZED)
            } else {
                profileDetailStatus
            }
            Log.d("UserDetailFragment", message)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
