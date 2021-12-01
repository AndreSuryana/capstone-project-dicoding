package com.dicoding.kasmee.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.ActivityLoginBinding
import com.dicoding.kasmee.ui.auth.register.RegisterActivity
import com.dicoding.kasmee.ui.main.MainActivity
import com.dicoding.kasmee.util.SessionManager
import com.dicoding.kasmee.util.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_Kasmee)
        setContentView(binding?.root)

        binding?.apply {
            btnLogin.setOnClickListener {
                login()
            }

            btnRegister.setOnClickListener {
                Intent(this@LoginActivity, RegisterActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        sessionManager.checkAuthToken(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun login() = lifecycleScope.launch {
        binding?.apply {
            val etEmail = etEmail
            val etPassword = etPassword

            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            when {
                email.isEmpty() -> etEmail.error = "Email tidak boleh kosong"
                password.isEmpty() -> etPassword.error = "Password tidak boleh kosong"
                else -> {
                    showProgressBar()
                    viewModel.login(email, password).observe(this@LoginActivity, { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                hideProgressBar()
                                resource.data?.data?.accessToken?.let { sessionManager.saveAuthToken(it) }
                                Intent(this@LoginActivity, MainActivity::class.java).also {
                                    it.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(it)
                                    finish()
                                }
                            }
                            Status.ERROR -> {
                                hideProgressBar()
                                showSnackBar(resource.message.toString())
                            }
                            Status.LOADING -> {
                                showProgressBar()
                            }
                        }
                    })
                }
            }
        }
    }

    private fun showProgressBar() {
        binding?.progressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        binding?.progressBar?.isVisible = false
    }

    private fun showSnackBar(message: String) {
        binding?.root?.let {
            Snackbar.make(
                it,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}