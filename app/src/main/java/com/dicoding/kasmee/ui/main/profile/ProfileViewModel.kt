package com.dicoding.kasmee.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: KasmeeRepository
) : ViewModel() {

    private var _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    private var _logout = MutableLiveData<Resource<Boolean>>()

    fun getUserInfo() {
        viewModelScope.launch {
            _user.value = Resource.loading()

            val result = repository.getUserInfo()

            if (result.data?.name.isNullOrEmpty()) {
                _user.value = result.message?.let { Resource.error(it) }
            } else {
                _user.value = Resource.success(result.data)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logout.value = Resource.loading()

            val result = repository.logout()

            if (result.data != true) {
                _logout.value = result.message?.let { Resource.error(it) }
            } else {
                _logout.value = Resource.success(result.data)
            }
        }
    }
}