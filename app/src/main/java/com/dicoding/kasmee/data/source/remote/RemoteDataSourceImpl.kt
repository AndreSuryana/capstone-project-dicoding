package com.dicoding.kasmee.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.data.model.response.transaction.TransactionResponse
import com.dicoding.kasmee.data.source.paging.CashPagingSource
import com.dicoding.kasmee.data.source.paging.TransactionPagingSource
import com.dicoding.kasmee.data.source.remote.api.ApiService
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

    override suspend fun getUserInfo(): Resource<User> {
        val response = apiService.getUserInfo()
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }

    override suspend fun getAllCash(): Resource<CashResponse> {
        val page = 1
        val response = apiService.getAllCash(page)
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }

    override fun getAllCashPager(): LiveData<PagingData<Cash>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CashPagingSource(apiService) }
        ).liveData

    override suspend fun addCash(
        name: String,
        userId: Int,
        target: Long
    ): Resource<Cash> {
        val response = apiService.addCash(name, userId, target)
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }

    override suspend fun updateCash(
        cashId: Int,
        name: String,
        userId: Int,
        target: Long
    ): Resource<Cash> {
        val response = apiService.updateCash(cashId, name, userId, target)
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }

    override suspend fun deleteCash(cashId: Int): Resource<Cash> {
        val response = apiService.deleteCash(cashId)
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }

    override suspend fun getAllTransaction(): Resource<TransactionResponse> {
        val page = 1
        val response = apiService.getAllTransaction(page)
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }

    override fun getAllTransactionPager(): LiveData<PagingData<Transaction>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TransactionPagingSource(apiService) }
        ).liveData

    override suspend fun addTransaction(
        cashId: Int,
        userId: Int,
        income: Long,
        outcome: Long,
        profit: Long,
        description: String
    ): Resource<Transaction> {
        val response =
            apiService.addTransaction(cashId, userId, income, outcome, profit, description)
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }

    override suspend fun updateTransaction(
        transactionId: Int,
        cashId: Int,
        userId: Int,
        income: Long,
        outcome: Long,
        profit: Long,
        description: String
    ): Resource<Transaction> {
        val response = apiService.updateTransaction(
            transactionId,
            cashId,
            userId,
            income,
            outcome,
            profit,
            description
        )
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }

    override suspend fun deleteTransaction(transactionId: Int): Resource<Transaction> {
        val response = apiService.deleteTransaction(transactionId)
        val result = response.body()

        return if (response.isSuccessful && result?.meta?.status == "success") {
            Resource.success(result.data)
        } else {
            Resource.error(response.message())
        }
    }
}