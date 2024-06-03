@file:Suppress("DEPRECATION")

package com.foundie.id.ui.login

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.foundie.id.viewmodel.LoginViewModelFactory
import com.foundie.id.MainActivity
import com.foundie.id.R
import com.foundie.id.ThemeActivity
import com.foundie.id.data.adapter.ImageSliderAdapter
import com.foundie.id.data.local.response.ImageDataResponse
import com.foundie.id.databinding.ActivityLoginBinding
import com.foundie.id.viewmodel.AuthModelFactory
import com.foundie.id.viewmodel.AuthViewModel
import com.foundie.id.settings.SETTINGS_KEY
import com.foundie.id.settings.SettingsPreferences
import com.foundie.id.settings.delayTime
import com.foundie.id.settings.getCurrentDateTime
import com.foundie.id.settings.updateStatusBarTheme
import com.foundie.id.ui.signup.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_KEY)
@Suppress("DEPRECATION")
class LoginActivity : ThemeActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var adapter: ImageSliderAdapter
    private val list = ArrayList<ImageDataResponse>()
    private lateinit var dots: ArrayList<TextView>
    private var currentPage = 0
    private val slideInterval = 1000L // Durasi antara perpindahan slide (dalam milidetik)
    private val signIn = 1001
    private val handler = Handler()
    private lateinit var runnable: Runnable
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, LoginViewModelFactory(this))[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateStatusBarTheme(window, resources.configuration)


        // Data Store
        val prefen = SettingsPreferences.getInstance(dataStore)
        val authViewModel =
            ViewModelProvider(this, AuthModelFactory(prefen))[AuthViewModel::class.java]

        // Gambar slider
        list.add(
            ImageDataResponse(
                "https://i.pinimg.com/564x/80/3e/d9/803ed9eaeeb0758b25c1a097aa2a07d2.jpg"
            )
        )
        list.add(
            ImageDataResponse(
                "https://i.pinimg.com/564x/cf/8a/0e/cf8a0e9c6ac0921688a0722cc90039f3.jpg"
            )
        )

        adapter = ImageSliderAdapter(list)
        binding.viewPager.adapter = adapter
        dots = ArrayList()
        setIndicator()

        // Atur pemutaran slide otomatis
        runnable = Runnable {
            if (currentPage == list.size) {
                currentPage = 0
            }
            binding.viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(runnable, slideInterval)
        }

        handler.postDelayed(runnable, slideInterval)

        // Menangani perpindahan halaman
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedDot(position)
                currentPage = position
                super.onPageSelected(position)
            }
        })

        // Login
        viewModel.loginStatus.observe(this) { loginStatus ->
            val isError = viewModel.isErrorLogin

            if (isError && !loginStatus.isNullOrEmpty()) Snackbar.make(
                binding.root,
                loginStatus,
                Snackbar.LENGTH_SHORT
            ).show()
            if (isError && !loginStatus.isNullOrEmpty() && loginStatus == "User not found") Snackbar.make(
                binding.root,
                getString(R.string.ERROR_PASSWORD_NOTSAME),
                Snackbar.LENGTH_SHORT
            ).show()
            else if (!isError && !loginStatus.isNullOrEmpty()) {
                val authLogin = viewModel.login.value
                val email = binding.etEmailLogin.text.toString()
                authViewModel.saveUserLoginSession(true)
                authViewModel.saveUserLoginToken(authLogin?.loginResult!!.token)
                authViewModel.saveUserLoginName(authLogin.loginResult.name)
                authViewModel.saveUserLoginRole(authLogin.loginResult.role)
                authViewModel.saveUserLoginEmail(email)
                authViewModel.saveUserLastLoginSession(getCurrentDateTime())
                Snackbar.make(binding.root, loginStatus, Snackbar.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }, delayTime)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.apply {
            btnLogin.setOnClickListener {
                val email = binding.etEmailLogin.text.toString()
                val password = binding.etPasswordLogin.text.toString()

                val emailError = if (email.isEmpty()) getString(R.string.ERROR_EMAIL_EMPTY)
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches()
                ) getString(R.string.ERROR_EMAIL_INVALID_FORMAT)
                else null

                val passwordError = if (password.isEmpty()) getString(R.string.ERROR_PASSWORD_EMPTY)
                else if (password.length < 8) getString(R.string.ERROR_PASSWORD_LENGTH)
                else null

                binding.etEmailLogin.error = emailError
                binding.etPasswordLogin.error = passwordError

                val allErrors = listOf(emailError, passwordError)
                val anyErrors = allErrors.any { it != null }

                if (anyErrors) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.ERROR_EDITEXT_EMPTY),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.login(email, password)
                }
            }

            tvSignUpNow.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }

            btnLoginGoogle.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, signIn)
            }
        }
    }

    // Fungsi untuk mengatur posisi dots slider berdasarkan gambarnya
    private fun selectedDot(position: Int) {
        val selectedColor = if (isDarkTheme()) ContextCompat.getColor(this, R.color.primayColorDark)
        else ContextCompat.getColor(this, R.color.primaryColor)

        for (i in 0 until list.size) {
            dots[i].setTextColor(if (i == position) selectedColor else secondaryTextColor())
        }
    }

    private fun isDarkTheme(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    // Mengatur warna dots
    private fun secondaryTextColor(): Int {
        return if (isDarkTheme()) ContextCompat.getColor(
            this,
            androidx.appcompat.R.color.secondary_text_default_material_dark
        )
        else ContextCompat.getColor(
            this,
            androidx.appcompat.R.color.secondary_text_default_material_light
        )
    }

    // Fungsi untuk mengatur dots-nya
    private fun setIndicator() {
        for (i in 0 until list.size) {
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("\u25CF", Html.FROM_HTML_MODE_LEGACY).toString()

            dots[i].textSize = 18f
            binding.dotsIndicator.addView(dots[i])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan pemutaran slide otomatis saat aktivitas dihancurkan
        handler.removeCallbacks(runnable)
    }

    // Fungsi Penanganan Google
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == signIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }, delayTime)

            // Lakukan sesuatu dengan informasi akun pengguna, misalnya validasi email
            // validateEmail(email)
        } catch (e: ApiException) {
            when (e.statusCode) {
                // Tambahkan case khusus jika diperlukan, contoh:
                GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> {
                    Snackbar.make(binding.root, getString(R.string.ERROR_LOGIN_GOOGLE), Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    Snackbar.make(
                        binding.root,
                        "Error ${e.statusCode}: ${e.localizedMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}