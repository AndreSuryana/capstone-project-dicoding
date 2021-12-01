package com.dicoding.kasmee.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.source.remote.RemoteDataSource
import com.dicoding.kasmee.util.Resource
import javax.inject.Inject

class KasmeeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : KasmeeRepository {

    override suspend fun login(
        email: String,
        password: String
    ): LiveData<Resource<Wrapper<AuthResponse>>> =
        remoteDataSource.login(email, password)

    override suspend fun register(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): LiveData<Resource<Wrapper<AuthResponse>>> =
        remoteDataSource.register(name, email, phoneNumber, password, confirmPassword)
}