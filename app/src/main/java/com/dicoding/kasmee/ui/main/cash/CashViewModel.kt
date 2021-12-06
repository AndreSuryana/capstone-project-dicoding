package com.dicoding.kasmee.ui.main.cash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashViewModel @Inject constructor(
    private val repository: KasmeeRepository
): ViewModel() {

    private var _cash = MutableLiveData<Resource<CashResponse>>()
    val cash: LiveData<Resource<CashResponse>> = _cash

    fun generateCash() {
        viewModelScope.launch {
            _cash.value = Resource.loading()

            val result = repository.getAllCash()

            if (result.data?.listCash?.isEmpty() == true) {
                _cash.value = result.message?.let { Resource.error(it) }
            } else {
                _cash.value = Resource.success(result.data)
            }
        }
    }
}