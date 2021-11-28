package com.dicoding.kasmee.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private var _text = MutableLiveData("Profile")
    val text: LiveData<String> = _text
}