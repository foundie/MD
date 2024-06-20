package com.foundie.id.ui.home.color_analysis.input

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.foundie.id.R
import com.foundie.id.data.adapter.ImageSliderAdapterColorCamera
import com.foundie.id.data.local.response.ImageDataResponseColor
import com.foundie.id.databinding.FragmentColorAnalysisInputSecondBinding
import com.foundie.id.settings.delayTimeSlider
import com.foundie.id.settings.selectedNumbers
import com.foundie.id.ui.home.color_analysis.result.ColorAnalysisOutputFragment
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.concurrent.ExecutorService

class ColorAnalysisInputSecondFragment : Fragment() {

    private var _binding: FragmentColorAnalysisInputSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ImageSliderAdapterColorCamera
    private val list = ArrayList<ImageDataResponseColor>()
    private lateinit var runnable: Runnable
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorAnalysisInputSecondBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        btnClick()

        binding.radioGroupOne.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_one_pick -> {
                    handleRadioButtonClick(1)
                }
                R.id.rb_two_pick -> {
                    handleRadioButtonClick(2)
                }
                R.id.rb_three_pick -> {
                    handleRadioButtonClick(3)
                }
                R.id.rb_four_pick -> {
                    handleRadioButtonClick(4)
                }
                R.id.rb_five_pick -> {
                    handleRadioButtonClick(5)
                }
            }
        }
    }

    private fun btnClick() {
        binding.apply {
            btnNext.setOnClickListener {
                val checkedId = radioGroupOne.checkedRadioButtonId
                if (checkedId == -1) {
                    Snackbar.make(root, getString(R.string.ERROR_COLOR_EMPTY), Snackbar.LENGTH_SHORT).show()
                } else {
                    val selectedNumber = when (checkedId) {
                        R.id.rb_one_pick -> 1
                        R.id.rb_two_pick -> 2
                        R.id.rb_three_pick -> 3
                        R.id.rb_four_pick -> 4
                        R.id.rb_five_pick -> 5
                        else -> -1
                    }

                    // Ambil list selectedNumbers dari arguments jika sudah ada, atau buat list baru jika belum ada
                    val selectedNumbers = arguments?.getIntegerArrayList("selected_numbers") ?: ArrayList()

                    // Tambahkan selectedNumber ke dalam list
                    selectedNumbers.add(selectedNumber)

                    // Buat bundle dan set data ke dalamnya
                    val bundle = Bundle().apply {
                        putIntegerArrayList("selected_numbers", selectedNumbers)
                    }

                    // Jika sudah mencapai langkah terakhir (misalnya langkah ke-7), lanjutkan ke fragment output
                    if (selectedNumbers.size >= 7) {
                        val fragment = ColorAnalysisOutputFragment()
                        fragment.arguments = bundle
                        replaceFragment(fragment)
                    } else {
                        // Jika belum mencapai langkah terakhir, lanjutkan ke langkah berikutnya (misalnya ke langkah ke-2)
                        val nextStepFragment = getNextStepFragment(selectedNumbers.size)
                        nextStepFragment.arguments = bundle
                        replaceFragment(nextStepFragment)
                    }
                }
            }
        }
    }

    private fun getNextStepFragment(step: Int): Fragment {
        return when (step) {
            1 -> ColorAnalysisInputSecondFragment()
            2 -> ColorAnalysisInputThirdFragment()
            3 -> ColorAnalysisInputFourthFragment()
            4 -> ColorAnalysisInputFifthFragment()
            5 -> ColorAnalysisInputSixthFragment()
            6 -> ColorAnalysisInputSeventhFragment()
            else -> throw IllegalArgumentException("Invalid step number: $step")
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupViewPager() {
        adapter = ImageSliderAdapterColorCamera(list)
        binding.viewPager.adapter = adapter

        runnable = object : Runnable {
            override fun run() {
                if (currentPage == list.size) currentPage = 0
                binding.viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, delayTimeSlider)
            }
        }

        handler.postDelayed(runnable, delayTimeSlider)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                super.onPageSelected(position)
            }
        })
    }

    private fun handleRadioButtonClick(number: Int) {
        Log.d("Selected number", "$number")
        selectedNumbers.add(number)
        val colorResList = when (number) {
            1 -> listOf(R.color.clarity_one_1, R.color.clarity_one_2, R.color.clarity_one_3, R.color.clarity_one_4)
            2 -> listOf(R.color.clarity_two_1, R.color.clarity_two_2, R.color.clarity_two_3, R.color.clarity_two_4)
            3 -> listOf(R.color.clarity_three_1, R.color.clarity_three_2, R.color.clarity_three_3, R.color.clarity_three_4)
            4 -> listOf(R.color.clarity_four_1, R.color.clarity_four_2, R.color.clarity_four_3, R.color.clarity_four_4)
            5 -> listOf(R.color.clarity_five_1, R.color.clarity_five_2, R.color.clarity_five_3, R.color.clarity_five_4)
            else -> listOf(R.color.white)
        }

        val colorList = colorResList.map { requireContext().getColor(it) }
        updateViewPagerWithColors(colorList)
    }

    private fun updateViewPagerWithColors(colors: List<Int>) {
        val newList = colors.map { ImageDataResponseColor(it) }
        adapter = ImageSliderAdapterColorCamera(newList)
        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)  // Remove callbacks to prevent memory leaks
        _binding = null
    }
}