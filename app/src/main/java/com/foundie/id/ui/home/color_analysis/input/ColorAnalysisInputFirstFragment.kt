package com.foundie.id.ui.home.color_analysis.input

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.foundie.id.R
import com.foundie.id.data.adapter.ImageSliderAdapterColorCamera
import com.foundie.id.data.local.response.ImageDataResponseColor
import com.foundie.id.databinding.FragmentColorAnalysisInputFirstBinding
import com.foundie.id.settings.delayTimeSlider
import com.foundie.id.settings.selectedNumbers
import com.foundie.id.ui.community.community_post.CreatePostFragment
import com.foundie.id.ui.home.color_analysis.result.ColorAnalysisOutputFragment
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ColorAnalysisInputFirstFragment : Fragment() {

    private var _binding: FragmentColorAnalysisInputFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ImageSliderAdapterColorCamera
    private val list = ArrayList<ImageDataResponseColor>()
    private lateinit var runnable: Runnable
    private var currentPostPhotoPath: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private val postPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                currentPostPhotoPath?.let {
                    val file = File(it)
                    binding.previewView.setImageURI(Uri.fromFile(file))
                }
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.IMAGE_SHOW_FAILED),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorAnalysisInputFirstBinding.inflate(inflater, container, false)
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

                    val selectedNumbers = arguments?.getIntegerArrayList("selected_numbers") ?: ArrayList()
                    selectedNumbers.add(selectedNumber)

                    val file = File(currentPostPhotoPath)
                    val photoURI = Uri.fromFile(file)

                    val bundle = Bundle().apply {
                        putIntegerArrayList("selected_numbers", selectedNumbers)
                        putString("photo_uri", photoURI.toString())
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

            previewView.setOnClickListener{
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    dispatchTakePictureIntent(isPost = true)
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        ColorAnalysisInputFirstFragment.REQUEST_CAMERA_PERMISSION
                    )
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
            1 -> listOf(R.color.brightness_one_1, R.color.brightness_one_2, R.color.brightness_one_3, R.color.brightness_one_4)
            2 -> listOf(R.color.brightness_two_1, R.color.brightness_two_2, R.color.brightness_two_3, R.color.brightness_two_4)
            3 -> listOf(R.color.brightness_three_1, R.color.brightness_three_2, R.color.brightness_three_3, R.color.brightness_three_4)
            4 -> listOf(R.color.brightness_four_1, R.color.brightness_four_2, R.color.brightness_four_3, R.color.brightness_four_4)
            5 -> listOf(R.color.brightness_five_1, R.color.brightness_five_2, R.color.brightness_five_3, R.color.brightness_five_4)
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

    private fun createImageFile(isPost: Boolean): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        val prefix = if (isPost) "Cam_" else "Gallery_"
        val file = File.createTempFile(
            "${prefix}${timeStamp}_",
            ".jpg",
            storageDir
        )

        currentPostPhotoPath = file.absolutePath

        return file
    }

    private fun dispatchTakePictureIntent(isPost: Boolean) {
        val photoFile: File? = try {
            createImageFile(isPost)
        } catch (ex: IOException) {
            null
        }
        photoFile?.also {
            val photoURI: Uri =
                FileProvider.getUriForFile(requireContext(), "com.foundie.id.fileprovider", it)
            postPhotoLauncher.launch(photoURI)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    dispatchTakePictureIntent(isPost = true)
                } else {
                    Snackbar.make(binding.root, "Camera permission denied", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)  // Remove callbacks to prevent memory leaks
        _binding = null
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }
}
