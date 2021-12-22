package com.dicoding.kasmee.ui.edit.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private var _isPasswordChanged = MutableLiveData<Boolean>()
    val isPasswordChanged: LiveData<Boolean> = _isPasswordChanged

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        viewModelScope.launch {
            val result = repository.changePassword(oldPassword, newPassword, confirmNewPassword)
            _isPasswordChanged.value = true
            _toastMessage.value = result.message.toString()
        }
    }
}