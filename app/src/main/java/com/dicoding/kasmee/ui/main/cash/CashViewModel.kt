package com.dicoding.kasmee.ui.main.cash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CashViewModel : ViewModel() {

    private var _text = MutableLiveData("Cash")
    val text: LiveData<String> = _text
}