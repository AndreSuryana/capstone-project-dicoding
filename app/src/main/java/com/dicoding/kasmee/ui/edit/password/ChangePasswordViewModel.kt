package com.dicoding.kasmee.ui.edit.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.data.repository.KasmeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repository: KasmeeRepository
): ViewModel() {

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        viewModelScope.launch {
            repository.changePassword(oldPassword, newPassword, confirmNewPassword)
        }
    }
}