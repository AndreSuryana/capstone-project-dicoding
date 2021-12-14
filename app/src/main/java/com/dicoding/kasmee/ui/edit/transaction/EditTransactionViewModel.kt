package com.dicoding.kasmee.ui.edit.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    private val repository: KasmeeRepository
): ViewModel() {

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>> = _toastText

    fun editTransaction(
        transactionId: Int,
        cashId: Int,
        income: Long,
        outcome: Long,
        description: String
    ) {
        viewModelScope.launch {
            // Validate input data
            when {
                income <= 0 -> {
                    _toastText.value = Event(R.string.income_is_zero)
                }
                outcome < 0 -> {
                    _toastText.value = Event(R.string.invalid_outcome)
                }
                else -> {
                    repository.updateTransaction(
                        transactionId = transactionId,
                        cashId = cashId,
                        income = income,
                        outcome = outcome,
                        profit = (income - outcome),
                        description = description
                    )
                }
            }
        }
    }
}