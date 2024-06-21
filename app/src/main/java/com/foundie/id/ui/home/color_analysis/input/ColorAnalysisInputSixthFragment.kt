package com.foundie.id.ui.home.color_analysis.input

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.foundie.id.R
import com.foundie.id.data.adapter.ImageSliderAdapterColorCamera
import com.foundie.id.data.local.response.ImageDataResponseColor
import com.foundie.id.databinding.FragmentColorAnalysisInputSixthBinding
import com.foundie.id.settings.delayTimeSlider
import com.foundie.id.settings.selectedNumbers
import com.foundie.id.ui.home.color_analysis.result.ColorAnalysisOutputFragment
import com.google.android.material.snackbar.Snackbar


class ColorAnalysisInputSixthFragment : Fragment() {

    private var _binding: FragmentColorAnalysisInputSixthBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ImageSliderAdapterColorCamera
    private val list = ArrayList<ImageDataResponseColor>()
    private lateinit var runnable: Runnable
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorAnalysisInputSixthBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            photoUri = bundle.getString("photo_uri")?.let { Uri.parse(it) }
            photoUri?.let { uri ->
                binding.previewView.setImageURI(uri)
            }
        }
        setupViewPager()
        btnClick()

        binding.radioGroupOne.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_one_pick -> {
                    handleRadioButtonClick(0)
                }

                R.id.rb_two_pick -> {
                    handleRadioButtonClick(1)
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
                        R.id.rb_one_pick -> 0
                        R.id.rb_two_pick -> 1
                        else -> -1
                    }


                    val selectedNumbers = arguments?.getIntegerArrayList("selected_numbers") ?: ArrayList()
                    selectedNumbers.add(selectedNumber)


                    val bundle = Bundle().apply {
                        putIntegerArrayList("selected_numbers", selectedNumbers)
                        putString("photo_uri",photoUri.toString())
                    }


                    if (selectedNumbers.size >= 7) {
                        val fragment = ColorAnalysisOutputFragment()
                        fragment.arguments = bundle
                        replaceFragment(fragment)
                    } else {
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
            0 -> listOf(R.color.persona_yellow_1)
            1 -> listOf(R.color.persone_yellow_2)
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