package com.foundie.id.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundie.id.data.adapter.ProfilePostAdapter
import com.foundie.id.data.local.response.PostsItem
import com.foundie.id.databinding.FragmentTweetBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.ProfileViewModelFactory
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class TweetFragment : Fragment() {
    private var _binding: FragmentTweetBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private lateinit var email: String

    private lateinit var adapter: ProfilePostAdapter
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
        _binding = FragmentTweetBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        adapter = ProfilePostAdapter()
        showRecyclerView()

        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        // Observasi token dan email dari AuthViewModel
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) { token ->
            Log.d("TweetFragment", "Token: $token")
            authViewModel.getUserLoginEmail().observe(viewLifecycleOwner) { email ->
                Log.d("TweetFragment", "Email: $email")
                // Setelah mendapatkan token dan email, panggil getDetailUser() pada viewModel
                viewModel.getDetailUser(token, email)
            }
        }


        viewModel.isLoadingDetail.observe(viewLifecycleOwner) {
            showLoading(it)
            Log.d("TweetFragment", "isLoadingProfile: $it")
        }

        viewModel.detailUserPost.observe(viewLifecycleOwner) { postProfileList ->
            Log.d("TweetFragment", "PostProfileList: ${postProfileList.size}")
            setProductData(postProfileList)
        }


        viewModel.profileStatus.observe(viewLifecycleOwner) { profileStatus ->
            if (profileStatus.isNullOrEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Error fetching profile data",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListCommunity.layoutManager = layoutManager
        binding.rvListCommunity.setHasFixedSize(true)
        binding.rvListCommunity.adapter = adapter
        Log.d("TweetFragment", "RecyclerView setup complete")
    }

    private fun setProductData(postProfileList: List<PostsItem>) {
        if (::adapter.isInitialized) {
            if (postProfileList.isNotEmpty()) {
                adapter.setData(postProfileList)
                Log.d("TweetFragment", "Adapter data set with ${postProfileList.size} items")
            } else {
                Log.d("TweetFragment", "No data to set in adapter")
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        Log.d("TweetFragment", "Loading visibility set to $isLoading")
    }
}
