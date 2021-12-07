package com.dicoding.kasmee.ui.main.cash

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.dicoding.kasmee.data.repository.KasmeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CashViewModel @Inject constructor(
    repository: KasmeeRepository
): ViewModel() {

    val cash = repository.getAllCashPager().cachedIn(viewModelScope)
}