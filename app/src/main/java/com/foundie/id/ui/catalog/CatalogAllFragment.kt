package com.foundie.id.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundie.id.data.adapter.CatalogAdapter
import com.foundie.id.data.local.response.ProductData
import com.foundie.id.databinding.FragmentCatalogAllBinding
import com.foundie.id.viewmodel.CatalogViewModelFactory

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

        viewModel.getProduct()
        viewModel.isLoadingProduct.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.productStatus.observe(viewLifecycleOwner) { productStatus ->
            val isError = viewModel.isErrorProduct

            if (isError && !productStatus.isNullOrEmpty()) {
                binding.rvListCatalog.visibility = View.VISIBLE
                setProductData(viewModel.product)
//                Snackbar.make(binding.root, productStatus, Snackbar.LENGTH_SHORT).show()

            } else if (!isError && !productStatus.isNullOrEmpty()) {
                binding.rvListCatalog.visibility = View.VISIBLE
                setProductData(viewModel.product)
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListCatalog.layoutManager = layoutManager
        binding.rvListCatalog.setHasFixedSize(true)
        binding.rvListCatalog.adapter = adapter
//        adapter.setOnItemClickCallback(object : CatalogAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: ProductData) {
//                //selectedStory(data)
//            }
//        })
    }

    private fun setProductData(ProductList: List<ProductData>) {
        if (::adapter.isInitialized) {
            if (ProductList.isNotEmpty()) {
//                binding.tvNotfound.visibility = View.GONE
//                binding.imgNotfound.visibility = View.GONE
                adapter.submitList(ProductList)
            } else {
//                binding.rvStory.visibility = View.GONE
//                binding.tvNotfound.visibility = View.VISIBLE
//                binding.imgNotfound.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}