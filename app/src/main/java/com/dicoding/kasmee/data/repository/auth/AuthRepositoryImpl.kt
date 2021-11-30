package com.dicoding.kasmee.data.repository.auth

import androidx.lifecycle.LiveData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.source.RemoteDataSource
import com.dicoding.kasmee.util.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : AuthRepository {

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