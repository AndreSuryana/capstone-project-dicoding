package com.dicoding.kasmee.ui.add.transaction

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
class AddTransactionViewModel @Inject constructor(
    private val repository: KasmeeRepository
) : ViewModel() {

    private var _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText

    fun addTransaction(cashId: Int, income: Long, outcome: Long, description: String) {
        viewModelScope.launch {
            // Validate input data
            when {
                income <= 0 -> {
                    _snackBarText.value = Event(R.string.income_is_zero)
                }
                outcome < 0 -> {
                    _snackBarText.value = Event(R.string.invalid_outcome)
                }
                else -> {
                    repository.addTransaction(
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