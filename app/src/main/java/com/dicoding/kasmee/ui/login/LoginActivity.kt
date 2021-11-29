package com.dicoding.kasmee.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.ActivityLoginBinding
import com.dicoding.kasmee.ui.main.MainActivity
import com.dicoding.kasmee.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_Kasmee)
        setContentView(binding?.root)

        binding?.btnLogin?.setOnClickListener {
            Intent(this@LoginActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }

        binding?.btnRegister?.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}