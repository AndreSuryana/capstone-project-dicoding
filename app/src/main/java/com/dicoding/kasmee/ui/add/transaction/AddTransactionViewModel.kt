package com.dicoding.kasmee.ui.add.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Event
import com.dicoding.kasmee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: KasmeeRepository
) : ViewModel() {

    private var _user = MutableLiveData<Resource<User>>()
    private val user: LiveData<Resource<User>> = _user

    private var _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText

    init {
        viewModelScope.launch {
            _user.value = repository.getUserInfo()
        }
    }

    fun addTransaction(cashId: Int, income: Long, outcome: Long, description: String) {
        viewModelScope.launch {
            // Validate input data
            if (income <= 0) {
                _snackBarText.value = Event(R.string.income_is_zero)
            } else if (outcome < 0) {
                _snackBarText.value = Event(R.string.invalid_outcome)
            } else {
                user.value?.data?.id?.let { userId ->
                    repository.addTransaction(
                        cashId = cashId,
                        userId = userId,
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