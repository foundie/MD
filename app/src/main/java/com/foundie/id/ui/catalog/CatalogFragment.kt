package com.foundie.id.ui.catalog

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.foundie.id.R
import com.foundie.id.databinding.FragmentCatalogBinding
import com.google.android.material.tabs.TabLayoutMediator

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private var extraSearch: String? = null
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.catalog)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val catalogPagerAdapter = CatalogPagerAdapter(requireActivity())
        binding.viewPager.adapter = catalogPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES_CATALOG[position])
        }.attach()

        (activity as? AppCompatActivity)?.supportActionBar?.elevation = 0f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Actionbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_catalog, menu)
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

    companion object {
        @StringRes
        private val TAB_TITLES_CATALOG = intArrayOf(
            R.string.all,
            R.string.popular
        )
    }
}
