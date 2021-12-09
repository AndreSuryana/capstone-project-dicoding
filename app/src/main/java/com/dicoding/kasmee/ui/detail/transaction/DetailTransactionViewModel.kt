package com.dicoding.kasmee.ui.detail.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.data.repository.KasmeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTransactionViewModel @Inject constructor(
    private val repository: KasmeeRepository
) : ViewModel() {

    private val _transactionId = MutableLiveData<Int>()

    private var _transaction = MutableLiveData<Transaction>()
    val transaction: LiveData<Transaction> = _transaction

    fun setTransaction(transaction: Transaction) {
        // Set transaction and transactionId
        _transaction.value = transaction
        _transactionId.value = transaction.id
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            _transactionId.value?.let { transactionId ->
                repository.deleteTransaction(transactionId)
            }
        }
    }
}