package com.dicoding.kasmee.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.transaction.Transaction
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

    private var _cash = MutableLiveData<Resource<List<Cash>>>()
    val cash: LiveData<Resource<List<Cash>>> = _cash

    private var _transaction = MutableLiveData<Resource<List<Transaction>>>()
    val transaction: LiveData<Resource<List<Transaction>>> = _transaction

    private var _todayTransaction = MutableLiveData<Resource<Transaction>>()
    val todayTransaction: LiveData<Resource<Transaction>> = _todayTransaction

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

            val result = repository.getLatestCash()

            if (result.data?.isEmpty() == true) {
                _cash.value = Resource.error("Kamu belum memiliki buku kas!")
            } else {
                _cash.value = Resource.success(result.data)
            }
        }
    }

    fun generateTransaction() {
        viewModelScope.launch {
            _transaction.value = Resource.loading()

            val result = repository.getLatestTransaction()

            if (result.data?.isEmpty() == true) {
                _transaction.value = Resource.error("Kamu belum pernah menambah transaksi!")
            } else {
                _transaction.value = Resource.success(result.data)
            }
        }
    }

    fun generateTodayTransaction(day: String) {
        viewModelScope.launch {
            _todayTransaction.value = Resource.loading()

            val result = repository.getTodayTransaction(day)

            if (result.data?.createdAt.isNullOrEmpty()) {
                _todayTransaction.value = Resource.error("Kamu belum membuat transaksi hari ini!")
            } else {
                _todayTransaction.value = Resource.success(result.data)
            }
        }
    }
}