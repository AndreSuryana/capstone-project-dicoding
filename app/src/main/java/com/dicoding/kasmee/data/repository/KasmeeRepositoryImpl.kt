package com.dicoding.kasmee.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.cash.CashResponse
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

    override suspend fun getUserInfo(): Resource<User> =
        remoteDataSource.getUserInfo()

    override suspend fun getAllCash(): Resource<CashResponse> =
        remoteDataSource.getAllCash()

    override fun getAllCashPager(): LiveData<PagingData<Cash>> =
        remoteDataSource.getAllCashPager()
}