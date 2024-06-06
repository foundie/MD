package com.foundie.id.ui.password

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.MainActivity
import com.foundie.id.R
import com.foundie.id.ThemeActivity
import com.foundie.id.databinding.ActivityPasswordBinding
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.delayTime
import com.foundie.id.ui.login.dataStore
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.viewmodel.PassViewModelFactory
import com.google.android.material.snackbar.Snackbar

class SetPasswordActivity : ThemeActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private lateinit var token: String
    private lateinit var email: String
    private val viewModel: PassViewModel by lazy {
        ViewModelProvider(this, PassViewModelFactory(this))[PassViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefen = SettingsPreferences.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]
        authViewModel.getUserLoginToken().observe(this) { token = it }
        authViewModel.getUserLoginEmail().observe(this) { email = it }

        setupUI()

        viewModel.passwordStatus.observe(this) { passwordStatus ->
            if (passwordStatus.isNullOrEmpty()) return@observe

            val isError = viewModel.isErrorPass
            val message = if (isError && passwordStatus == "Unauthorized") {
                getString(R.string.ERROR_LOGIN_GOOGLE)
            } else {
                passwordStatus
            }

            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

            if (!isError) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(
                        Intent(this@SetPasswordActivity, MainActivity::class.java)
                        .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
                }, delayTime)
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            btnSignUp.setOnClickListener {
                val password = etPasswordSignUp.text.toString()
                val confirmPassword = etPasswordConfirmSignUp.text.toString()

                if (validateInputs(password, confirmPassword)) {
                    viewModel.setPassword(token, email, password)
                }
            }
        }
    }

    private fun validateInputs(password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (password.isEmpty()) {
            binding.etPasswordSignUp.error = getString(R.string.ERROR_PASSWORD_EMPTY)
            isValid = false
        } else if (password.length < 8) {
            binding.etPasswordSignUp.error = getString(R.string.ERROR_PASSWORD_LENGTH)
            isValid = false
        } else {
            binding.etPasswordSignUp.error = null
        }

        if (confirmPassword.isEmpty()) {
            binding.etPasswordConfirmSignUp.error = getString(R.string.ERROR_PASSWORD_EMPTY)
            isValid = false
        } else if (password != confirmPassword) {
            binding.etPasswordConfirmSignUp.error = getString(R.string.ERROR_PASSWORD_MISMATCH)
            isValid = false
        } else {
            binding.etPasswordConfirmSignUp.error = null
        }

        return isValid
    }
}