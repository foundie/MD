package com.foundie.id.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundie.id.R
import com.foundie.id.data.adapter.GroupCommunityAdapter
import com.foundie.id.data.adapter.UserPostAdapter
import com.foundie.id.data.local.response.DataPostItem
import com.foundie.id.data.local.response.GroupsDataItem
import com.foundie.id.databinding.FragmentCommunityListGroupBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.login.dataStore
import com.foundie.id.ui.profile.user_detail.UserDetailFragment
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.CommunityViewModelFactory
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class  CommunityListGroupFragment : Fragment() {
    private var _binding: FragmentCommunityListGroupBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String

    private lateinit var adapter: GroupCommunityAdapter
    private val viewModel: CommunityViewModel by lazy {
        ViewModelProvider(this, CommunityViewModelFactory(requireContext()))[CommunityViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityListGroupBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        adapter = GroupCommunityAdapter()
        showRecyclerView()

        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            viewModel.getListGroup(token)
        }

        viewModel.isLoadingListGroup.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.listGroup.observe(viewLifecycleOwner) { groupList ->
            setPostData(groupList)
        }

        viewModel.listGroupStatus.observe(viewLifecycleOwner) { listGroupStatus ->
            val isError = viewModel.isErrorListGroup

            if (isError && !listGroupStatus.isNullOrEmpty()) {
                binding.rvListCommunityGroup.visibility = View.VISIBLE
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(),2)
        binding.rvListCommunityGroup.layoutManager = layoutManager
        binding.rvListCommunityGroup.setHasFixedSize(true)
        binding.rvListCommunityGroup.adapter = adapter
    }
//        adapter.setOnItemClickCallback(object : CatalogAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: ProductData) {
//                //selectedStory(data)
//            }
//        })

    private fun setPostData(groupList: List<GroupsDataItem>) {
        if (::adapter.isInitialized) {
            if (groupList.isNotEmpty()) {
                adapter.setData(groupList)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}