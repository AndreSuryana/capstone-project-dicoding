package com.dicoding.kasmee.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.ActivityRegisterBinding
import com.dicoding.kasmee.ui.main.MainActivity
import com.dicoding.kasmee.util.SessionManager
import com.dicoding.kasmee.util.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding
    private val viewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_Kasmee)
        setContentView(binding?.root)

        binding?.apply {
            btnRegister.setOnClickListener {
                register()
            }

            btnBack.setOnClickListener {
                finish()
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun register() = lifecycleScope.launch {
        binding?.apply {
            val etName = etName
            val etEmail = etEmail
            val etPhone = etPhone
            val etPassword = etPassword
            val etRepassword = etRepassword

            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etRepassword.text.toString()

            when {
                name.isEmpty() -> etName.error = getString(R.string.error_name)
                email.isEmpty() -> etEmail.error = getString(R.string.error_email)
                phone.isEmpty() -> etPhone.error = getString(R.string.error_phone_number)
                password.isEmpty() -> etPassword.error = getString(R.string.error_password)
                confirmPassword.isEmpty() -> etRepassword.error =
                    getString(R.string.error_confirm_password)
                else -> {
                    showProgressBar()
                    viewModel.register(name, email, phone, password, confirmPassword)
                        .observe(this@RegisterActivity, { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    hideProgressBar()
                                    resource.data?.data?.accessToken?.let { sessionManager.saveAuthToken(it) }
                                    Intent(this@RegisterActivity, MainActivity::class.java).also {
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
            )
        }
    }
}