package com.foundie.id.ui.home.compare_product


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.R
import com.foundie.id.databinding.FragmentCompareProductInputBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.home.makeup_analysis.PredictViewModel
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.PredictViewModelFactory
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class CompareProductInputFragment : Fragment() {

    private var _binding: FragmentCompareProductInputBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences
    private lateinit var token: String
    private val items = listOf("lip", "face", "foundation & cussion", "cheek", "powder", "eye")
    private lateinit var autoComplete: AutoCompleteTextView

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
        _binding = FragmentCompareProductInputBinding.inflate(inflater, container, false)

        autoComplete = binding.actvType
        val adapter = ArrayAdapter(requireContext(), R.layout.item_list, items)
        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedItems = items[position]
            autoComplete.setText(selectedItems)
        }


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

        viewModel.isLoadingFilter.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.filterStatus.observe(viewLifecycleOwner) { filterStatus ->
            if (!filterStatus.isNullOrEmpty()) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.POST_UPLOAD_SUCCESS),
                    Snackbar.LENGTH_SHORT
                ).show()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    replaceFragment(CommunityFragment())
//                }, delayTime)
            } else if (viewModel.isErrorFilter && !filterStatus.isNullOrEmpty()) {
                Snackbar.make(binding.root, filterStatus, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnClick() {
        binding.apply {
            btnFilter.setOnClickListener {
                val title = etPtitle.text.toString().trim()
                val type = autoComplete.text.toString().trim()
                val brand = etNameBrand.text.toString().trim()
                val variant = etVariant.text.toString().trim()

                if (title.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.ERROR_TITLE_DESC_EMPTY),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val titleBody = title.toRequestBody("text/plain".toMediaType())
                val typeBody = type.toRequestBody("text/plain".toMediaType())
                val brandBody = brand.toRequestBody("text/plain".toMediaType())
                val variantBody = variant.toRequestBody("text/plain".toMediaType())

                viewModel.filter(token, titleBody, typeBody, brandBody, variantBody)
            }
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
}
