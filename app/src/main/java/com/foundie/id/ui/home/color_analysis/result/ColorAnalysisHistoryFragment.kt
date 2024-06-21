package com.foundie.id.ui.home.color_analysis.result

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.R
import com.foundie.id.databinding.FragmentColorAnalysisHistoryBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.loadImageWithCacheBusting
import com.foundie.id.ui.home.makeup_analysis.PredictViewModel
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.PredictViewModelFactory

class ColorAnalysisHistoryFragment : Fragment() {

    private var _binding: FragmentColorAnalysisHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences

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
        _binding = FragmentColorAnalysisHistoryBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        binding.WcHistory.visibility = View.GONE
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)

        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) { token ->
            token?.let {
                viewModel.getHistory(it)
            }
        }

        viewModel.isLoadingHistory.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.historyStatus.observe(viewLifecycleOwner) { colorStatus ->
            if (colorStatus.isNullOrEmpty()){
                binding.WcHistory.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                return@observe
            }

            val isError = viewModel.isErrorColors
            val message = if (isError && colorStatus == "Unauthorized") {
                getString(R.string.ERROR_UNAUTHORIZED)
            } else {
                colorStatus
            }
            Log.d("ColorAnalysisHistoryFragment", message)
        }

        viewModel.historyColorAnalysis.observe(viewLifecycleOwner) { history ->
            if (history != null && history.type == "Color Analysis") {
                binding.apply {
                    tvTitleSeasonResult.text = history.colorSeason
                    tvInspoDescriptionDetail.text = history.description
                    tvSeasonPersentageOne.text = history.seasonCompatibilityPercentages.autumnDeep.toString() + "%"
                    tvSeasonPersentageTwo.text = history.seasonCompatibilityPercentages.winterDeep.toString() + "%"
                    tvSeasonPersentageThree.text = history.seasonCompatibilityPercentages.springLight.toString() + "%"
                    tvSeasonPersentageFour.text = history.seasonCompatibilityPercentages.summerLight.toString() + "%"
                    tvSeasonPersentageFive.text = history.seasonCompatibilityPercentages.autumnSoft.toString() + "%"
                    tvSeasonPersentageSix.text = history.seasonCompatibilityPercentages.summerSoft.toString() + "%"
                    tvSeasonPersentageSeven.text = history.seasonCompatibilityPercentages.springClear.toString() + "%"
                    tvSeasonPersentageEight.text = history.seasonCompatibilityPercentages.winterClear.toString() + "%"
                    tvSeasonPersentageNine.text = history.seasonCompatibilityPercentages.autumnWarm.toString() + "%"
                    tvSeasonPersentageTen.text = history.seasonCompatibilityPercentages.springWarm.toString() + "%"
                    tvSeasonPersentageEleven.text = history.seasonCompatibilityPercentages.summerCool.toString() + "%"
                    tvSeasonPersentageTwelve.text = history.seasonCompatibilityPercentages.winterCool.toString() + "%"
                    ivColorOne.setBackgroundColor(Color.parseColor(history.palette[0]))
                    ivColorTwo.setBackgroundColor(Color.parseColor(history.palette[1]))
                    ivColorThree.setBackgroundColor(Color.parseColor(history.palette[2]))
                    ivColorFour.setBackgroundColor(Color.parseColor(history.palette[3]))
                    ivColorFive.setBackgroundColor(Color.parseColor(history.palette[4]))
                    ivColorSix.setBackgroundColor(Color.parseColor(history.palette[5]))
                    ivColorSeven.setBackgroundColor(Color.parseColor(history.palette[6]))
                    ivColorEight.setBackgroundColor(Color.parseColor(history.palette[7]))
                    ivColorNine.setBackgroundColor(Color.parseColor(history.palette[8]))
                    ivColorTen.setBackgroundColor(Color.parseColor(history.palette[9]))
                    ivColorEleven.setBackgroundColor(Color.parseColor(history.palette[10]))
                    ivColorTwelve.setBackgroundColor(Color.parseColor(history.palette[11]))
                    ivInspo.loadImageWithCacheBusting(history.seasonImage)
                    binding.WcHistory.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        _binding = null
    }
}