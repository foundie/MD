package com.foundie.id.ui.community

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
import com.foundie.id.ui.community.community_detail.CommunityDetailFragment
import com.foundie.id.ui.login.dataStore
import com.foundie.id.ui.profile.user_detail.UserDetailFragment
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.CommunityViewModelFactory
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class CommunityListGroupFragment : Fragment() {
    private var _binding: FragmentCommunityListGroupBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private var extraSearch: String? = null
    private lateinit var searchView: SearchView

    private lateinit var adapter: GroupCommunityAdapter
    private val viewModel: CommunityViewModel by lazy {
        ViewModelProvider(
            this, CommunityViewModelFactory(requireContext())
        )[CommunityViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail_community, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = getString(R.string.search_hint)
        searchView.maxWidth = Integer.MAX_VALUE

        if (!extraSearch.isNullOrEmpty()) {
            searchView.run {
                onActionViewExpanded()
                requestFocusFromTouch()
                setQuery(extraSearch, false)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                closeKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                return false
            }
        })

        val searchIconResId = R.drawable.ic_menu_search
        menu.findItem(R.id.menu_search).icon =
            ContextCompat.getDrawable(requireContext(), searchIconResId)
    }

    private fun closeKeyboard() {
        val view = activity?.currentFocus
        view?.let {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun filterList(query: String) {
        (binding.rvListCommunityGroup.adapter as? GroupCommunityAdapter)?.filter(query)
    }

    private fun showRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListCommunityGroup.layoutManager = layoutManager
        binding.rvListCommunityGroup.setHasFixedSize(true)
        binding.rvListCommunityGroup.adapter = adapter
        adapter.setOnItemClickCallback(object : GroupCommunityAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GroupsDataItem) {
                val bundle = Bundle().apply {
                    putParcelable(CommunityDetailFragment.EXTRA_GROUP, data)
                }
                val fragment = CommunityDetailFragment().apply {
                    arguments = bundle
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

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