package com.dicoding.kasmee.data.source.remote

import androidx.lifecycle.LiveData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.util.Resource

interface RemoteDataSource {

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

    suspend fun getUserInfo(): Resource<User>

    suspend fun getAllCash(): Resource<CashResponse>
}