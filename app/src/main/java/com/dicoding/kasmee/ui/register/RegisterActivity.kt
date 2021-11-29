package com.dicoding.kasmee.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.ActivityRegisterBinding
import com.dicoding.kasmee.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_Kasmee)
        setContentView(binding?.root)

        binding?.btnRegister?.setOnClickListener {
            Intent(this@RegisterActivity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        binding?.btnBack?.setOnClickListener { onBackClicked() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onBackClicked() {
        finish()
    }
}