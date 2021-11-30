package com.dicoding.kasmee.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.ActivityLoginBinding
import com.dicoding.kasmee.ui.auth.register.RegisterActivity
import com.dicoding.kasmee.ui.main.MainActivity

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