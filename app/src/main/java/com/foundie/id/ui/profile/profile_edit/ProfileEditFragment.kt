package com.foundie.id.ui.profile.profile_edit

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
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
import com.foundie.id.ui.profile.ProfileViewModel
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.ProfileViewModelFactory
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private var currentImageUri: Uri? = null
    private var getFileUriStory: File? = null
    private lateinit var fileImage: File
    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())
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
            val myFile = uriToFile(uri, requireContext())
            getFileUriStory = myFile
            showImage()
        } else {
            Snackbar.make(
                binding.root, getString(R.string.IMAGE_SHOW_FAILED), Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private val items = arrayOf("Male", "Female", "Prefer not to say")
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)

        autoCompleteTextView = binding.actvGender
        adapterItems = ArrayAdapter(requireContext(), R.layout.item_list, items)
        autoCompleteTextView.setAdapter(adapterItems)

        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), "Selected: $item", Toast.LENGTH_SHORT).show()
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
            viewModel.getBiodata(token)
        }

        viewModel.isLoadingeditBiodata.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.editprofileStatus.observe(viewLifecycleOwner) { editprofileStatus ->
            if (!editprofileStatus.isNullOrEmpty() && editprofileStatus == "Biodata updated successfully") {
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
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val file = getFileUriStory as File

                        var compressedFile: File? = null
                        var compressedFileSize = file.length()
                        while (compressedFileSize > 1 * 1024 * 1024) {
                            compressedFile = withContext(Dispatchers.Default) {
                                Compressor.compress(requireContext().applicationContext, file)
                            }
                            compressedFileSize = compressedFile.length()
                        }

                        fileImage = compressedFile ?: file
                    }

                    val imageCompressFile =
                        fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val profile: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photoProfile", fileImage.name, imageCompressFile
                    )
                    val cover: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photoCover", fileImage.name, imageCompressFile
                    )
                    val name = binding.tvName.text.toString().trim()
                    val phone = binding.tvName.text.toString().trim()
                    val location = binding.tvName.text.toString().trim()
                    val gender = binding.tvName.text.toString().trim()

                    viewModel.editBiodata(token, cover, profile, name, phone, location, gender)
                }
            }
            ivBackgroundProfile.setOnClickListener {
                startGallery()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun showImage() {
        this.currentImageUri?.let {
            binding.ivBackgroundProfile.setImageURI(it)
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

    companion object {
        const val FILENAME_FORMAT = "MMddyyyy"
    }
}
