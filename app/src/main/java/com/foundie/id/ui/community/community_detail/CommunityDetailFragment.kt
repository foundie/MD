package com.foundie.id.ui.community.community_detail

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.foundie.id.R
import com.foundie.id.data.local.response.GroupsDataItem
import com.foundie.id.databinding.FragmentCommunityDetailBinding
import com.foundie.id.settings.loadImageWithCacheBusting

class CommunityDetailFragment : Fragment() {

    private var _binding: FragmentCommunityDetailBinding? = null
    private val binding get() = _binding!!
    private var group: GroupsDataItem? = null

    private var extraSearch: String? = null
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            group = it.getParcelable(EXTRA_GROUP)
        }
        super.onViewCreated(view, savedInstanceState)
        group?.let {
            setGroupDetails(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                // Implement your search logic here
                closeKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
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

    private fun setGroupDetails(group: GroupsDataItem) {
        binding.apply {
            tvCommunityName.text = group.title
            tvCommunityDescription.text = group.description
            tvMember.text = group.subscription.toString()
            val topics = group.topics.joinToString(", ")
            tvTopics.text = topics
            ivUser.loadImageWithCacheBusting(group.profileImageUrl)
            ivBackgroundUser.loadImageWithCacheBusting(group.coverImageUrl)
        }
    }

    companion object {
        const val EXTRA_GROUP = "extra_group"
    }
}
