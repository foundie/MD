package com.foundie.id.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.ViewModelProvider
import com.foundie.id.R
import com.foundie.id.viewmodel.SignUpViewModelFactory
import com.foundie.id.ThemeActivity
import com.foundie.id.databinding.ActivitySignUpBinding
import com.foundie.id.settings.delayTime
import com.foundie.id.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class SignUpActivity : ThemeActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this, SignUpViewModelFactory(this))[SignUpViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            btnSignUp.setOnClickListener {
                if (!cbTerms.isChecked) {
                    Snackbar.make(root, getString(R.string.ERROR_AGREEMENT_NOT_CHECKED), Snackbar.LENGTH_SHORT).show()
                    vibrate()
                    return@setOnClickListener
                }
                val name = etNameSignUp.text.toString()
                val email = etEmailSignUp.text.toString()
                val password = etPasswordSignUp.text.toString()
                val confirmPassword = etPasswordConfirmSignUp.text.toString()

                if (validateInputs(name, email, password, confirmPassword)) {
                    viewModel.register(name, email, password)
                }
            }

            tvSignInNow.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            }
        }

    viewModel.registerStatus.observe(this) { registerStatus ->
            if (registerStatus.isNullOrEmpty()) return@observe

            val isError = viewModel.isErrorRegister
            val message = if (isError && registerStatus == "Email already exists") {
                getString(R.string.ERROR_EMAIL_ALREADY_REGISTERED)
            } else {
                registerStatus
            }

            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

            if (!isError) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java)
                        .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
                }, delayTime)
            }
        }
    }

    private fun validateInputs(name: String, email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.etNameSignUp.error = getString(R.string.ERROR_NAME_EMPTY)
            isValid = false
        } else if (name.length > 25) {
            binding.etNameSignUp.error = getString(R.string.ERROR_NAME_TOOLONG)
            isValid = false
        } else {
            binding.etNameSignUp.error = null // Clear error if valid
        }

        if (email.isEmpty()) {
            binding.etEmailSignUp.error = getString(R.string.ERROR_EMAIL_EMPTY)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailSignUp.error = getString(R.string.ERROR_EMAIL_INVALID_FORMAT)
            isValid = false
        } else {
            binding.etEmailSignUp.error = null // Clear error if valid
        }

        if (password.isEmpty()) {
            binding.etPasswordSignUp.error = getString(R.string.ERROR_PASSWORD_EMPTY)
            isValid = false
        } else if (password.length < 8) {
            binding.etPasswordSignUp.error = getString(R.string.ERROR_PASSWORD_LENGTH)
            isValid = false
        } else {
            binding.etPasswordSignUp.error = null // Clear error if valid
        }

        if (confirmPassword.isEmpty()) {
            binding.etPasswordConfirmSignUp.error = getString(R.string.ERROR_PASSWORD_EMPTY)
            isValid = false
        } else if (password != confirmPassword) {
            binding.etPasswordConfirmSignUp.error = getString(R.string.ERROR_PASSWORD_MISMATCH)
            isValid = false
        } else {
            binding.etPasswordConfirmSignUp.error = null // Clear error if valid
        }

        return isValid
    }


    // Fungsi Getar Perangkat
    private fun vibrate() {
        val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.run { vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } else {
            vibrator.vibrate(500)
        }
    }
}
