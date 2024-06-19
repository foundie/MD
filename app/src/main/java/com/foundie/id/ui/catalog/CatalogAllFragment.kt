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
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.foundie.id.R
import com.foundie.id.data.adapter.CatalogAdapter
import com.foundie.id.data.local.response.ProductData
import com.foundie.id.databinding.FragmentCatalogAllBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.CatalogViewModelFactory

@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
class  CatalogAllFragment : Fragment() {
    private var _binding: FragmentCatalogAllBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private var extraSearch: String? = null
    private lateinit var searchView: SearchView

    private lateinit var adapter: CatalogAdapter
    private val viewModel: CatalogViewModel by lazy {
        ViewModelProvider(this, CatalogViewModelFactory(requireContext()))[CatalogViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogAllBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        adapter = CatalogAdapter()
        showRecyclerView()
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            viewModel.getProduct(token)
        }

        viewModel.isLoadingProduct.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.product.observe(viewLifecycleOwner) { productList ->
            setProductData(productList)
        }

        viewModel.productStatus.observe(viewLifecycleOwner) { productStatus ->
            val isError = viewModel.isErrorProduct

            if (isError && !productStatus.isNullOrEmpty()) {
                binding.rvListCatalog.visibility = View.VISIBLE
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListCatalog.layoutManager = layoutManager
        binding.rvListCatalog.setHasFixedSize(true)
        binding.rvListCatalog.adapter = adapter
//        adapter.setOnItemClickCallback(object : CatalogAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: ProductData) {
//                //selectedStory(data)
//            }
//        })
    }

    @Deprecated("Deprecated in Java")
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

    private fun filterList(query: String) {
        (binding.rvListCatalog.adapter as? CatalogAdapter)?.filter(query)
    }

    private fun closeKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setProductData(productList: List<ProductData>) {
        if (::adapter.isInitialized) {
            if (productList.isNotEmpty()) {
                adapter.setData(productList)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}