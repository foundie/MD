package com.foundie.id.ui.community.community_post

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.foundie.id.R
import com.foundie.id.databinding.FragmentCreatePostBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.delayTime
import com.foundie.id.ui.community.CommunityFragment
import com.foundie.id.ui.community.CommunityViewModel
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.CommunityViewModelFactory
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Suppress("DEPRECATION", "SameParameterValue")
class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private var currentPostPhotoPath: String? = null
    private var postImageUri: Uri? = null

    private val viewModel: CommunityViewModel by lazy {
        ViewModelProvider(
            this,
            CommunityViewModelFactory(requireContext())
        )[CommunityViewModel::class.java]
    }

    private val profilePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                currentPostPhotoPath?.let {
                    val file = File(it)
                    binding.ivImage.setImageURI(Uri.fromFile(file))
                }
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.IMAGE_SHOW_FAILED),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    private val profileGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                binding.ivImage.setImageURI(it)
                postImageUri = it
                currentPostPhotoPath = uriToFile(it).absolutePath
            } ?: run {
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
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)

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
        btnClick()
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
        }

        viewModel.isLoadingAddPost.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.addPostStatus.observe(viewLifecycleOwner) { addpostStatus ->
            if (!addpostStatus.isNullOrEmpty() && addpostStatus == "Post successfully created") {
                Snackbar.make(
                    binding.root,
                    getString(R.string.POST_UPLOAD_SUCCESS),
                    Snackbar.LENGTH_SHORT
                ).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    replaceFragment(CommunityFragment())
                }, delayTime)
            } else if (viewModel.isErrorAddPost && !addpostStatus.isNullOrEmpty()) {
                Snackbar.make(binding.root, addpostStatus, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnClick() {
        binding.apply {
            tvPost.setOnClickListener {
                val title = edTitle.text.toString().trim()
                val desc = edDescription.text.toString().trim()

                if (title.isEmpty() || desc.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.ERROR_TITLE_DESC_EMPTY),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val postImagePath = currentPostPhotoPath

                if (postImagePath != null) {
                    lifecycleScope.launch {
                        try {
                            val postFile = File(postImagePath)

                            val compressedProfileFile =
                                if (postFile.length() > 1 * 1024 * 1024) {
                                    withContext(Dispatchers.IO) {
                                        Compressor.compress(
                                            requireContext().applicationContext,
                                            postFile
                                        )
                                    }
                                } else {
                                    postFile
                                }

                            val postImageBody =
                                compressedProfileFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

                            val postFileName =
                                "${System.currentTimeMillis()}_${UUID.randomUUID()}_post.jpg"

                            val postImagePart = MultipartBody.Part.createFormData(
                                "image",
                                postFileName,
                                postImageBody
                            )

                            val titleBody = title.toRequestBody("text/plain".toMediaType())
                            val descBody = desc.toRequestBody("text/plain".toMediaType())

                            viewModel.addPostUser(token, postImagePart, titleBody, descBody)
                        } catch (e: IOException) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.ERROR_COMPRESSING_DATA),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.ERROR_UPLOAD_DATA),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            ivUploadCamera.setOnClickListener {
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
                        REQUEST_CAMERA_PERMISSION
                    )
                }
            }

            ivUploadPhoto.setOnClickListener {
                profileGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
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
            profilePhotoLauncher.launch(photoURI)
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

            REQUEST_GALLERY_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    profileGalleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                } else {
                    Snackbar.make(binding.root, "Gallery permission denied", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = requireContext().contentResolver
        val file = File.createTempFile("temp_image", ".jpg", requireContext().cacheDir)
        contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        _binding = null
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
        private const val REQUEST_GALLERY_PERMISSION = 101
    }
}
