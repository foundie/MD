package com.foundie.id.ui.profile.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.R
import com.foundie.id.databinding.FragmentSettingBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.ui.login.LoginActivity
import com.foundie.id.ui.login.dataStore
import com.foundie.id.ui.profile.ProfileFragment
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefen: SettingsPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.title = getString(R.string.Settings)
        prefen = SettingsPreferences.getInstance(requireContext().dataStore)
        btnClick()
        return binding.root
    }


    private fun btnClick() {
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        binding.apply {
            tvLogout.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                val alert = builder.create()
                builder
                    .setTitle(getString(R.string.LOGOUT_CONFIRMATION_TITLE))
                    .setMessage(getString(R.string.LOGOUT_CONFIRMATION_MESSAGE))
                    .setPositiveButton(getString(R.string.LOGOUT_CONFIRMATION_YES)) { _, _ ->
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        authViewModel.deleteUserLoginSession()
                    }
                    .setNegativeButton(getString(R.string.LOGOUT_CONFIRMATION_CANCEL)) { _, _ ->
                        alert.cancel()
                    }
                    .show()
            }
            tvLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                replaceFragment(ProfileFragment())
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _binding = null
    }
}
