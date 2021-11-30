package com.dicoding.kasmee.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.network.ApiService
import com.dicoding.kasmee.util.Resource
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {

    override suspend fun login(
        email: String,
        password: String
    ): LiveData<Resource<Wrapper<AuthResponse>>> {
        val resultLogin = MutableLiveData<Resource<Wrapper<AuthResponse>>>()
        val response = apiService.login(email, password)
        val result = response.body()

        if (response.isSuccessful && response.body()?.meta?.status == "success") {
            resultLogin.value = result.let { Resource.success(it) }
        } else {
            resultLogin.value = Resource.error(response.message())
        }

        return resultLogin
    }

    override suspend fun register(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): LiveData<Resource<Wrapper<AuthResponse>>> {
        val resultRegister = MutableLiveData<Resource<Wrapper<AuthResponse>>>()
        val response = apiService.register(name, email, phoneNumber, password, confirmPassword)
        val result = response.body()

        if (response.isSuccessful && response.body()?.meta?.status == "success") {
            resultRegister.value = result.let { Resource.success(it) }
        } else {
            resultRegister.value = Resource.error(response.message())
        }

        return resultRegister
    }
}