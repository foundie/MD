package com.foundie.id.ui.home.makeup_analysis.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.foundie.id.R
import com.foundie.id.data.adapter.RecomendationAdapter
import com.foundie.id.data.local.response.RecomendationDataItem
import com.foundie.id.databinding.FragmentSkinToneBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.home.makeup_analysis.PredictViewModel
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.PredictViewModelFactory

class SkinToneFragment : Fragment() {

    private var _binding: FragmentSkinToneBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private lateinit var adapter: RecomendationAdapter
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
        _binding = FragmentSkinToneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        adapter = RecomendationAdapter()
        showRecyclerView()
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            viewModel.getHistory(token)
        }

        viewModel.isLoadingHistory.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.historySkinTone.observe(viewLifecycleOwner) { history ->
            if (history != null && history.type == "skin tone") {
                binding.apply {
                    tvTitleResult.text = history.result
                    tvDescriptionResult.text = history.message
                    setProductData(history.product)
                }
            }
        }

        viewModel.historyStatus.observe(viewLifecycleOwner) { historyStatus ->
            if (historyStatus.isNullOrEmpty()){
                binding.ivIcon.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                return@observe
            }

            val isError = viewModel.isErrorHistory
            val message = if (isError && historyStatus == "Unauthorized") {
                getString(R.string.ERROR_UNAUTHORIZED)
            } else {
                historyStatus
            }
            Log.d("SkinToneFragment", message)
        }
    }

    private fun showRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListRecomendation.layoutManager = layoutManager
        binding.rvListRecomendation.setHasFixedSize(true)
        binding.rvListRecomendation.adapter = adapter
    }

    private fun setProductData(recomendationList: List<RecomendationDataItem>) {
        if (::adapter.isInitialized) {
            if (recomendationList.isNotEmpty()) {
                Log.d("SkinToneFragment", "data tidak kosong")
                adapter.setData(recomendationList)
                Log.d("SkinToneFragment", "Product data set successfully")
                // Mencetak data produk ke dalam log
                for (product in recomendationList) {
                    Log.d("SkinToneFragment", "Product ID: ${product.brand}, Name: ${product.variantName}, Price: ${product.season1Name}")
                }
            } else {
                Log.d("SkinToneFragment", "Product data is empty")
            }
        } else {
            Log.e("SkinToneFragment", "RecomendationAdapter is not initialized")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
