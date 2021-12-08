package com.dicoding.kasmee.ui.main.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dicoding.kasmee.data.repository.KasmeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    repository: KasmeeRepository
): ViewModel() {

    val transaction = repository.getAllTransactionPager().cachedIn(viewModelScope)
}