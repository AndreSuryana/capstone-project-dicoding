package com.dicoding.kasmee.ui.main.home

import androidx.lifecycle.*
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KasmeeRepository
): ViewModel() {

    private var _cash = MutableLiveData<Resource<Wrapper<CashResponse>>>()
    val cash: LiveData<Resource<Wrapper<CashResponse>>> = _cash

    fun generateCash() {
        viewModelScope.launch {
            _cash.value = repository.cash().value
        }
    }
}