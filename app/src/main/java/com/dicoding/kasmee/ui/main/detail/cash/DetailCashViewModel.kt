package com.dicoding.kasmee.ui.main.detail.cash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.transaction.TransactionResponse
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Event
import com.dicoding.kasmee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCashViewModel @Inject constructor(
    private val repository: KasmeeRepository
) : ViewModel() {

    private var _transaction = MutableLiveData<Resource<TransactionResponse>>()
    val transaction: LiveData<Resource<TransactionResponse>> = _transaction

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText

    private val _undoDelete = MutableLiveData<Event<Cash>>()
    val undoDelete: LiveData<Event<Cash>> = _undoDelete

    fun getTransaction(cashId: Int) {
        viewModelScope.launch {
            _transaction.value = Resource.loading()

            val result = repository.getAllTransactionByCashId(cashId)

            if (result.data?.listTransaction?.isEmpty() == true) {
                _transaction.value = Resource.error("Kamu belum pernah menambah transaksi!")
            } else {
                _transaction.value = Resource.success(result.data)
            }
        }
    }

    fun deleteCash(cash: Cash) {
        viewModelScope.launch {
            repository.deleteCash(cash.id)
            _snackBarText.value = Event(R.string.cash_deleted)
            _undoDelete.value = Event(cash)
        }
    }

    fun addCash(cash: Cash) {
        viewModelScope.launch {
            repository.addCash(
                cash.name,
                cash.userId,
                cash.target
            )
        }
    }
}