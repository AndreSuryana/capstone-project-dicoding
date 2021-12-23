package com.dicoding.kasmee.ui.main.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySettingsBinding.inflate(layoutInflater)
//        setTheme(R.style.Theme_Kasmee)
        setContentView(binding?.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        binding?.btnBack?.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}