package com.dicoding.kasmee.ui.detail.cash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.transaction.Transaction
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

    private val _cashId = MutableLiveData<Int>()

    private var _cash = MutableLiveData<Cash>()
    val cash: LiveData<Cash> = _cash

    private var _transaction = MutableLiveData<Resource<TransactionResponse>>()
    val transaction: LiveData<Resource<TransactionResponse>> = _transaction

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText

    private val _undoDelete = MutableLiveData<Event<Transaction>>()
    val undoDelete: LiveData<Event<Transaction>> = _undoDelete

    fun setCashId(cashId: Int) {
        _cashId.value = cashId
    }

    fun getCash() {
        viewModelScope.launch {
            val result = _cashId.value?.let { cashId ->
                repository.getCashById(cashId)
            }
            result?.data.let { cash ->
                _cash.value = cash
            }
        }
    }

    fun getTransaction() {
        viewModelScope.launch {
            _transaction.value = Resource.loading()

            val result = _cashId.value?.let { cashId ->
                repository.getAllTransactionByCashId(cashId)
            }

            if (result?.data?.listTransaction?.isEmpty() == true) {
                _transaction.value = Resource.error("Kamu belum pernah menambah transaksi!")
            } else {
                _transaction.value = Resource.success(result?.data)
            }
        }
    }

    fun deleteCash() {
        viewModelScope.launch {
            cash.value?.let { cash ->
                repository.deleteCash(cash.id)
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction.id)
            _undoDelete.value = Event(transaction)
            _snackBarText.value = Event(R.string.transaction_deleted)
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.addTransaction(
                transaction.cashId,
                transaction.income,
                transaction.outcome,
                transaction.profit,
                transaction.description
            )
        }
    }
}