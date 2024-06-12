package com.foundie.id.ui.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundie.id.data.adapter.CatalogAdapter
import com.foundie.id.data.local.response.ProductData
import com.foundie.id.databinding.FragmentCatalogAllBinding
import com.foundie.id.viewmodel.CatalogViewModelFactory
import com.google.android.material.snackbar.Snackbar

class  CatalogAllFragment : Fragment() {
    private var _binding: FragmentCatalogAllBinding? = null
    private val binding get() = _binding!!

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
        adapter = CatalogAdapter()
        showRecyclerView()

        viewModel.isLoadingProduct.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.product.observe(viewLifecycleOwner) { productList ->
            setProductData(productList)
        }
        viewModel.getProduct()

        viewModel.productStatus.observe(viewLifecycleOwner) { productStatus ->
            val isError = viewModel.isErrorProduct

            if (isError && !productStatus.isNullOrEmpty()) {
                binding.rvListCatalog.visibility = View.VISIBLE
                Snackbar.make(binding.root, productStatus, Snackbar.LENGTH_SHORT).show()

            } else if (!isError && !productStatus.isNullOrEmpty()) {
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

    private fun setProductData(productList: List<ProductData>) {
        if (::adapter.isInitialized) {
            if (productList.isNotEmpty()) {
                Log.d("HomeFragment","data tidak kosong")
                adapter.setData(productList)
                Log.d("HomeFragment", "Product data set successfully")
                // Mencetak data produk ke dalam log
                for (product in productList) {
                    Log.d("HomeFragment", "Product ID: ${product.brand}, Name: ${product.variantName}, Price: ${product.season1Name}")
                }
            } else {
                Log.d("HomeFragment", "Product data is empty")
            }
        } else {
            Log.e("HomeFragment", "CatalogAdapter is not initialized")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}