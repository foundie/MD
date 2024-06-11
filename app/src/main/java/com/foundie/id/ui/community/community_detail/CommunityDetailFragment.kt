package com.foundie.id.ui.community.community_detail

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
import com.foundie.id.R
import com.foundie.id.databinding.FragmentCommunityDetailBinding

class CommunityDetailFragment : Fragment() {

    private var _binding: FragmentCommunityDetailBinding? = null
    private val binding get() = _binding!!

    private var extraSearch: String? = null
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.community) //ini bisa ga sih dihilangin, jadi cuma ada search aja iconnya di action bar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Actionbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail_community, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = getString(R.string.search_hint)
        searchView.maxWidth = Integer.MAX_VALUE

        if (extraSearch != null && extraSearch != "") {
            searchView.run {
                onActionViewExpanded()
                requestFocusFromTouch()
                setQuery(extraSearch, false)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Implement your search logic here
                // For example: catalogViewModel.searchCatalogItems(query)
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
        if (view != null) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
