package com.dicoding.kasmee.ui.edit.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.databinding.ActivityEditProfileBinding
import com.dicoding.kasmee.util.Ext.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding
    private val viewModel: EditProfileViewModel by viewModels()

    // Variable data validation
    private var isValid: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Button Listener
        binding?.apply {
            btnBack.setOnClickListener(this@EditProfileActivity)
            btnSave.setOnClickListener(this@EditProfileActivity)
        }

        // Observe current user info
        viewModel.setUser()
        viewModel.user.observe(this, Observer(this::setCurrentUserInfo))
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
                updateUserInfo()
            }
        }
    }

    private fun setCurrentUserInfo(user: User) {
        binding?.apply {
            ivProfileImage.loadImage(user.profilePhotoUrl)
            etName.setText(user.name)
            etEmail.setText(user.email)
            etPhone.setText(user.phoneNumber)
        }
    }

    private fun updateUserInfo() {
        // Get value from edit text
        val name = binding?.etName?.text?.trim().toString()
        val email = binding?.etEmail?.text?.trim().toString()
        val phoneNumber = binding?.etPhone?.text?.trim().toString()

        isValid = true

        when {
            name.isEmpty() -> {
                binding?.etName?.error = getString(R.string.empty_profile_name)
                isValid = false
            }
            email.isEmpty() -> {
                binding?.etEmail?.error = getString(R.string.empty_profile_email)
                isValid = false
            }
            phoneNumber.isEmpty() -> {
                binding?.etPhone?.error = getString(R.string.empty_profile_phone)
                isValid = false
            }
            else -> {
                if (isValid) {
                    viewModel.updateUserInfo(name, email, phoneNumber)
                    Toast.makeText(applicationContext, getString(R.string.profile_change_success), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}