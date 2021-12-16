package com.dicoding.kasmee.ui.edit.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.repository.KasmeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: KasmeeRepository
): ViewModel() {

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun setUser() {
        viewModelScope.launch {
            repository.getUserInfo().data?.let {
                _user.value = it
            }
        }
    }

    fun updateUserInfo(
        name: String,
        email: String,
        phoneNumber: String,
        password: String
    ) {
        viewModelScope.launch {
            repository.updateprofile(name, email, phoneNumber, password)
        }
    }
}