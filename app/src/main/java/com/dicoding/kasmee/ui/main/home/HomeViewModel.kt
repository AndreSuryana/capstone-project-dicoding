package com.dicoding.kasmee.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.home.CashHomeResponse
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KasmeeRepository
): ViewModel() {

    private var _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    private var _cash = MutableLiveData<Resource<CashHomeResponse>>()
    val cash: LiveData<Resource<CashHomeResponse>> = _cash

    // This for the future resources :
    // private var _transaction = MutableLiveData<Resource<Transaction>>()
    // val transaction: LiveData<Transaction> = _transaction

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

    fun generateCash() {
        viewModelScope.launch {
            _cash.value = Resource.loading()

            val result = repository.home()

            if (result.data?.listCash?.isEmpty() == true) {
                _cash.value = result.message?.let { Resource.error(it) }
            } else {
                _cash.value = Resource.success(result.data)
            }
        }
    }
}