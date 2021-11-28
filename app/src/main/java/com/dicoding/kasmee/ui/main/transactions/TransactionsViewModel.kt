package com.dicoding.kasmee.ui.main.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransactionsViewModel : ViewModel() {

    private var _text = MutableLiveData("Transactions")
    val text: LiveData<String> = _text
}