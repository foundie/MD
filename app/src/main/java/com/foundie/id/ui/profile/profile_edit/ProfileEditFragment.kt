package com.foundie.id.ui.profile.profile_edit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.foundie.id.R
import com.foundie.id.databinding.FragmentProfileEditBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.delayTime
import com.foundie.id.ui.login.dataStore
import com.foundie.id.ui.navigation.FragmentActivity
import com.foundie.id.ui.profile.ProfileFragment
import com.foundie.id.ui.profile.ProfilePagerAdapter
import com.foundie.id.ui.profile.ProfileViewModel
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.ProfileViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private var currentImageUri: Uri? = null
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(
            this,
            ProfileViewModelFactory(requireContext())
        )[ProfileViewModel::class.java]
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            this.currentImageUri = uri
            binding.ivBackgroundProfile.setImageURI(null)
        } else {
            Snackbar.make(
                binding.root, getString(R.string.IMAGE_SHOW_FAILED), Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)

        (activity as? AppCompatActivity)?.supportActionBar?.elevation = 0f

        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(viewLifecycleOwner) {
            token = it
            viewModel.getBiodata(token)
        }

        viewModel.isLoadingeditBiodata.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.editprofileStatus.observe(this) { editprofileStatus ->
            if (!editprofileStatus.isNullOrEmpty() && editprofileStatus == "Story created successfully") {
                Snackbar.make(
                    binding.root, getString(R.string.IMAGE_UPLOAD_SUCCESS), Snackbar.LENGTH_SHORT
                ).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(requireActivity(), FragmentActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }, delayTime)
            } else if (viewModel.isErroreditBiodata && !editprofileStatus.isNullOrEmpty()) {
                Snackbar.make(binding.root, editprofileStatus, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnClick() {
        binding.apply {
            btnSave.setOnClickListener {

                if (requireActivity().currentImageUri == null) {
                    Snackbar.make(
                        binding.root, getString(R.string.IMAGE_UPLOAD_EMPTY), Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val name = binding.tvName.text.toString().trim()

                if (caption.isEmpty()) {
                    binding.tvCaption.error = resources.getString(R.string.CAPTION_UPLOAD_EMPTY)
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val file = getFileUriStory as File

                        var compressedFile: File? = null
                        var compressedFileSize = file.length()
                        while (compressedFileSize > 1 * 1024 * 1024) {
                            compressedFile = withContext(Dispatchers.Default) {
                                Compressor.compress(applicationContext, file)
                            }
                            compressedFileSize = compressedFile.length()
                        }

                        fileImage = compressedFile ?: file

                    }

                    val imageCompressFile =
                        fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val image: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo", fileImage.name, imageCompressFile
                    )
                    val caption = caption.toRequestBody("text/plain".toMediaType())
                    viewModel.uploadStory(
                        token, image, caption, latlng?.latitude, latlng?.longitude
                    )
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
