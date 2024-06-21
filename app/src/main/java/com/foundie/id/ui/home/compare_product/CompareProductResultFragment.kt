package com.foundie.id.ui.home.compare_product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.foundie.id.data.adapter.CompareProductAdapter
import com.foundie.id.data.local.response.SimilarProductsItem
import com.foundie.id.databinding.FragmentCompareProductResultBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.home.makeup_analysis.PredictViewModel
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.PredictViewModelFactory
import com.google.android.material.snackbar.Snackbar

class CompareProductResultFragment : Fragment() {

    private var _binding: FragmentCompareProductResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private lateinit var adapter: CompareProductAdapter

    private val viewModel: PredictViewModel by lazy {
        ViewModelProvider(
            this,
            PredictViewModelFactory(requireContext())
        )[PredictViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareProductResultBinding.inflate(inflater, container, false)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)

        (activity as? AppCompatActivity)?.supportActionBar?.elevation = 0f
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        adapter = CompareProductAdapter()
        showRecyclerView()
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            val index = arguments?.getInt(EXTRA_INDEX)
            if (index != null) {
                viewModel.getCompare(token, index)
            }
        }

        viewModel.isLoadingCompare.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.compareProduct.observe(viewLifecycleOwner) { postList ->
            setPostData(postList)
        }

        viewModel.compareStatus.observe(viewLifecycleOwner) { compareStatus ->
            if (!compareStatus.isNullOrEmpty()) {
                Log.d("CompareProductProcessFragment", "Load Images Success")
            } else if (viewModel.isErrorCompare && !compareStatus.isNullOrEmpty()) {
                Snackbar.make(binding.root, compareStatus, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListCatalog.layoutManager = layoutManager
        binding.rvListCatalog.setHasFixedSize(true)
        binding.rvListCatalog.adapter = adapter
    }

    private fun setPostData(compareList: List<SimilarProductsItem>) {
        if (::adapter.isInitialized) {
            if (compareList.isNotEmpty()) {
                adapter.setData(compareList)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_INDEX = "extra_index"
    }
}
