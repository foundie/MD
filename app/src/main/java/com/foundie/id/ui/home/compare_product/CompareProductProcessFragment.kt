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
import com.foundie.id.R
import com.foundie.id.data.adapter.FilterAdapter
import com.foundie.id.data.local.response.DataFilterProduct
import com.foundie.id.databinding.FragmentCompareProductProcessBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.home.makeup_analysis.PredictViewModel
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.PredictViewModelFactory
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class CompareProductProcessFragment : Fragment() {

    private var _binding: FragmentCompareProductProcessBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private lateinit var adapter: FilterAdapter

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
        _binding = FragmentCompareProductProcessBinding.inflate(inflater, container, false)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }

        Log.d("CompareProductProcessFragment", "onCreateView: Binding created")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)

        (activity as? AppCompatActivity)?.supportActionBar?.elevation = 0f
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        adapter = FilterAdapter()
        showRecyclerView()
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it

            val title = arguments?.getString(EXTRA_TITLE)
            val type = arguments?.getString(EXTRA_TYPE)
            val brand = arguments?.getString(EXTRA_BRAND)
            val variant = arguments?.getString(EXTRA_VARIANT)

            val titleBody = title?.toRequestBody("text/plain".toMediaType())
            val typeBody = type?.toRequestBody("text/plain".toMediaType())
            val brandBody = brand?.toRequestBody("text/plain".toMediaType())
            val variantBody = variant?.toRequestBody("text/plain".toMediaType())

            viewModel.filter(token, titleBody, typeBody, brandBody, variantBody)
        }

        viewModel.isLoadingFilter.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.filterProduct.observe(viewLifecycleOwner) { postList ->
            Log.d("CompareProductProcessFragment", "Data received: $postList")
            setPostData(postList)
        }

        viewModel.filterStatus.observe(viewLifecycleOwner) { filterStatus ->
            if (!filterStatus.isNullOrEmpty()) {
                Snackbar.make(
                    binding.root,
                    "Berhasil",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else if (viewModel.isErrorFilter && !filterStatus.isNullOrEmpty()) {
                Snackbar.make(binding.root, filterStatus, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListCatalog.layoutManager = layoutManager
        binding.rvListCatalog.setHasFixedSize(true)
        binding.rvListCatalog.adapter = adapter
        Log.d("CompareProductProcessFragment", "RecyclerView setup completed")
    }

    private fun setPostData(filterList: List<DataFilterProduct>) {
        Log.d("CompareProductProcessFragment", "setPostData: Data received: $filterList")
        if (::adapter.isInitialized) {
            if (filterList.isNotEmpty()) {
                adapter.setData(filterList)
                Log.d("CompareProductProcessFragment", "Adapter set with data")
            } else {
                Log.d("CompareProductProcessFragment", "Filter list is empty")
            }
        } else {
            Log.d("CompareProductProcessFragment", "Adapter is not initialized")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_BRAND = "extra_brand"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_VARIANT = "extra_variant"
    }
}
