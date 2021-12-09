package com.dicoding.kasmee.ui.add.cash

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
class AddCashViewModel @Inject constructor(
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

    fun addCash(name: String, target: Long) {
        viewModelScope.launch {
            // Validate input data
            if (name.isEmpty() && target <= 0) { // When all the input is invalid
                _snackBarText.value = Event(R.string.check_input_message)
            } else if (name.isEmpty()) { // When just name is empty
                _snackBarText.value = Event(R.string.empty_cash_name)
            } else if (target <= 0) { // Target should not be minus or 0
                _snackBarText.value = Event(R.string.zero_target_error)
            } else { // All the input is valid
                user.value?.data?.id?.let { userId ->
                    repository.addCash(name, userId, target)
                }
            }
        }
    }
}