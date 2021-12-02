package com.dicoding.kasmee.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.util.Resource

interface KasmeeRepository {

    suspend fun login(
        email: String,
        password: String
    ): LiveData<Resource<Wrapper<AuthResponse>>>

    suspend fun register(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): LiveData<Resource<Wrapper<AuthResponse>>>

    suspend fun cash(): LiveData<Resource<Wrapper<CashResponse>>>
}