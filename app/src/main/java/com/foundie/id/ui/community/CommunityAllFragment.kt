package com.foundie.id.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundie.id.R
import com.foundie.id.data.adapter.UserPostAdapter
import com.foundie.id.data.local.response.DataPostItem
import com.foundie.id.databinding.FragmentCommunityAllBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.login.dataStore
import com.foundie.id.ui.profile.user_detail.UserDetailFragment
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.CommunityViewModelFactory

@Suppress("DEPRECATION")
class CommunityAllFragment : Fragment() {
    private var _binding: FragmentCommunityAllBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String

    private lateinit var adapter: UserPostAdapter
    private val viewModel: CommunityViewModel by lazy {
        ViewModelProvider(
            this,
            CommunityViewModelFactory(requireContext())
        )[CommunityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityAllBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        adapter = UserPostAdapter()
        showRecyclerView()

        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            viewModel.getPostUser(token)
        }

        viewModel.isLoadingUserPost.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.userPost.observe(viewLifecycleOwner) { postList ->
            setPostData(postList)
        }

        viewModel.userPostStatus.observe(viewLifecycleOwner) { postStatus ->
            val isError = viewModel.isErrorPost

            if (isError && !postStatus.isNullOrEmpty()) {
                binding.rvListCommunity.visibility = View.VISIBLE
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListCommunity.layoutManager = layoutManager
        binding.rvListCommunity.setHasFixedSize(true)
        binding.rvListCommunity.adapter = adapter
        adapter.setOnItemClickCallback(object : UserPostAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataPostItem) {
                TODO("Not yet implemented")
            }

            override fun onProfileImageClicked(data: DataPostItem) {
                val email = data.email
                val bundle = Bundle().apply {
                    putString(UserDetailFragment.EXTRA_EMAIL, email)
                }
                val fragment = UserDetailFragment().apply {
                    arguments = bundle
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit()
            }
        })
    }

    private fun setPostData(postList: List<DataPostItem>) {
        if (::adapter.isInitialized) {
            if (postList.isNotEmpty()) {
                adapter.setData(postList)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}