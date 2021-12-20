package com.dicoding.kasmee.ui.edit.password

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.ActivityChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityChangePasswordBinding? = null
    private val binding get() = _binding
    private val viewModel: ChangePasswordViewModel by viewModels()

    // Variable data validation
    private var isValid: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Button Listener
        binding?.apply {
            btnBack.setOnClickListener(this@ChangePasswordActivity)
            btnSave.setOnClickListener(this@ChangePasswordActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                finish()
            }
            R.id.btn_save -> {
                changePassword()
            }
        }
    }

    private fun changePassword() {
        // Get value from edit text
        val oldPassword = binding?.etOldPassword?.text?.trim().toString()
        val newPassword = binding?.etNewPassword?.text?.trim().toString()
        val confirmNewPassword = binding?.etReNewPassword?.text?.trim().toString()

        isValid = true

        when {
            oldPassword.isEmpty() -> {
                binding?.etOldPassword?.error = getString(R.string.empty_field)
                binding?.etOldPassword?.requestFocus()
                isValid = false
            }
            newPassword.isEmpty() -> {
                binding?.etNewPassword?.error = getString(R.string.empty_field)
                binding?.etNewPassword?.requestFocus()
                isValid = false
            }
            confirmNewPassword.isEmpty() -> {
                binding?.etReNewPassword?.error = getString(R.string.empty_field)
                binding?.etReNewPassword?.requestFocus()
                isValid = false
            }
            else -> {
                if (isValid) {
                    viewModel.changePassword(oldPassword, newPassword, confirmNewPassword)
                    finish()
                }
            }
        }
    }
}