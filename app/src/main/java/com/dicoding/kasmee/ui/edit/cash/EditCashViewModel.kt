package com.dicoding.kasmee.ui.edit.cash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCashViewModel @Inject constructor(
    private val repository: KasmeeRepository
) : ViewModel() {

    private var _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>> = _toastText

    private var _cash = MutableLiveData<Cash>()
    val cash: LiveData<Cash> = _cash

    fun setCash(cashId: Int) {
        viewModelScope.launch {
            repository.getCashById(cashId).data?.let {
                _cash.value = it
            }
        }
    }

    fun editCash(cashId: Int, name: String, target: Long) {
        viewModelScope.launch {
            // Validate input data
            if (name.isEmpty() && target <= 0) { // When all the input is invalid
                _toastText.value = Event(R.string.check_input_message)
            } else if (name.isEmpty()) { // When just name is empty
                _toastText.value = Event(R.string.empty_cash_name)
            } else if (target <= 0) { // Target should not be minus or 0
                _toastText.value = Event(R.string.zero_target_error)
            } else { // All the input is valid
                repository.updateCash(cashId, name, target)
            }
        }
    }
}