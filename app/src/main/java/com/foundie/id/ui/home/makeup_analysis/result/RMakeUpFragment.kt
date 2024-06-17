package com.foundie.id.ui.home.makeup_analysis.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.R
import com.foundie.id.databinding.FragmentRMakeUpBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.home.makeup_analysis.PredictViewModel
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.PredictViewModelFactory

class RMakeUpFragment : Fragment() {

    private var _binding: FragmentRMakeUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
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
        _binding = FragmentRMakeUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            viewModel.getHistory(token)
        }

        viewModel.isLoadingHistory.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.history.observe(viewLifecycleOwner) { history ->
            if (history != null && history.type == "face classification") {
                binding.apply {
                    tvTitleResult.text = history.prediction
                    tvDescriptionResult.text = history.message
                }
            }
        }

        viewModel.historyStatus.observe(viewLifecycleOwner) { historyStatus ->
            if (historyStatus.isNullOrEmpty()) return@observe

            val isError = viewModel.isErrorHistory
            val message = if (isError && historyStatus == "Unauthorized") {
                getString(R.string.ERROR_UNAUTHORIZED)
            } else {
                historyStatus
            }
            Log.d("RMakeUpFragment", message)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}