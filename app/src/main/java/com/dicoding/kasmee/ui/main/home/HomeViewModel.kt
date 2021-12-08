package com.dicoding.kasmee.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.data.model.response.transaction.TransactionResponse
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KasmeeRepository
) : ViewModel() {

    private var _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    private var _cash = MutableLiveData<Resource<CashResponse>>()
    val cash: LiveData<Resource<CashResponse>> = _cash

    private var _transaction = MutableLiveData<Resource<TransactionResponse>>()
    val transaction: LiveData<Resource<TransactionResponse>> = _transaction

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

            val result = repository.getAllCash()

            if (result.data?.listCash?.isEmpty() == true) {
                _cash.value = Resource.error("Kamu belum memiliki buku kas!")
            } else {
                _cash.value = Resource.success(result.data)
            }
        }
    }

    fun generateTransaction() {
        viewModelScope.launch {
            _transaction.value = Resource.loading()

            val result = repository.getAllTransaction()

            if (result.data?.listTransaction?.isEmpty() == true) {
                _transaction.value = Resource.error("Kamu belum pernah menambah transaksi!")
            } else {
                _transaction.value = Resource.success(result.data)
            }
        }
    }
}