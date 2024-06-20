package com.foundie.id.ui.home.color_analysis.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundie.id.R
import com.foundie.id.data.adapter.ColorPaletteAdapter
import com.foundie.id.databinding.FragmentColorAnalysisOutputBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.loadImageWithCacheBusting
import com.foundie.id.ui.home.makeup_analysis.PredictViewModel
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.PredictViewModelFactory

class ColorAnalysisOutputFragment : Fragment() {

    private var _binding: FragmentColorAnalysisOutputBinding? = null
    private val binding get() = _binding!!
    private lateinit var colorAdapter: ColorPaletteAdapter
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
        _binding = FragmentColorAnalysisOutputBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)

        val selectedNumbers = arguments?.getIntegerArrayList("selected_numbers") ?: emptyList()

        val indexZeroValue = selectedNumbers.getOrNull(0)?.toString() ?: "0"
        val indexOneValue = selectedNumbers.getOrNull(1)?.toString() ?: "0"
        val indexTwoValue = selectedNumbers.getOrNull(2)?.toString() ?: "0"
        val indexThreeValue = selectedNumbers.getOrNull(3)?.toString() ?: "0"
        val indexFourValue = selectedNumbers.getOrNull(4)?.toString() ?: "0"
        val indexFiveValue = selectedNumbers.getOrNull(5)?.toString() ?: "0"
        val indexSixValue = selectedNumbers.getOrNull(6)?.toString() ?: "0"

        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) { token ->
            token?.let {
                viewModel.colorAnalysis(
                    it,
                    indexZeroValue.toInt(),
                    indexFourValue.toInt(),
                    indexFiveValue.toInt(),
                    indexThreeValue.toInt(),
                    indexSixValue.toInt(),
                    indexTwoValue.toInt(),
                    indexOneValue.toInt()
                )
            }
        }

        viewModel.isLoadingColorAnalysis.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        setupRecyclerView()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnClick()

        viewModel.colorAnalysis.observe(viewLifecycleOwner) { colorAnalysis ->
            if (colorAnalysis != null) {
                binding.apply {
                    tvTitleSeasonResult.text = colorAnalysis.colorSeason
                    tvInspoDescriptionDetail.text = colorAnalysis.description
                    tvSeasonPersentageOne.text = colorAnalysis.seasonCompatibilityPercentages.autumnDeep.toString() + "%"
                    tvSeasonPersentageTwo.text = colorAnalysis.seasonCompatibilityPercentages.winterDeep.toString() + "%"
                    tvSeasonPersentageThree.text = colorAnalysis.seasonCompatibilityPercentages.springLight.toString() + "%"
                    tvSeasonPersentageFour.text = colorAnalysis.seasonCompatibilityPercentages.summerLight.toString() + "%"
                    tvSeasonPersentageFive.text = colorAnalysis.seasonCompatibilityPercentages.autumnSoft.toString() + "%"
                    tvSeasonPersentageSix.text = colorAnalysis.seasonCompatibilityPercentages.summerSoft.toString() + "%"
                    tvSeasonPersentageSeven.text = colorAnalysis.seasonCompatibilityPercentages.springClear.toString() + "%"
                    tvSeasonPersentageEight.text = colorAnalysis.seasonCompatibilityPercentages.winterClear.toString() + "%"
                    tvSeasonPersentageNine.text = colorAnalysis.seasonCompatibilityPercentages.autumnWarm.toString() + "%"
                    tvSeasonPersentageTen.text = colorAnalysis.seasonCompatibilityPercentages.springWarm.toString() + "%"
                    tvSeasonPersentageEleven.text = colorAnalysis.seasonCompatibilityPercentages.summerCool.toString() + "%"
                    tvSeasonPersentageTwelve.text = colorAnalysis.seasonCompatibilityPercentages.winterCool.toString() + "%"
                    ivInspo.loadImageWithCacheBusting(colorAnalysis.seasonImage)
                    showColorPalette(colorAnalysis.palette)
                    mainLayout.visibility = View.VISIBLE
                }
            }
        }

        viewModel.colorAnalysisStatus.observe(viewLifecycleOwner) { colorStatus ->
            if (colorStatus.isNullOrEmpty()) return@observe

            val isError = viewModel.isErrorColors
            val message = if (isError && colorStatus == "Unauthorized") {
                getString(R.string.ERROR_UNAUTHORIZED)
            } else {
                colorStatus
            }
            Log.d("ColorAnalysisOutputFragment", message)
        }
    }

    private fun btnClick() {
        binding.apply {

        }
    }

    private fun setupRecyclerView() {
        colorAdapter = ColorPaletteAdapter(emptyList())
        binding.rvListColorPalette.adapter = colorAdapter
        binding.rvListColorPalette.setHasFixedSize(true)
        binding.rvListColorPalette.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // Set up RecyclerView layout manager and other properties if needed
    }


    private fun showColorPalette(palette: List<String>) {
        Log.d("ColorAnalysisOutputFragment", "Received color palette: $palette")
        colorAdapter.updateData(palette)
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
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