package com.dicoding.kasmee.data.source.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.data.model.response.transaction.TransactionResponse
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

    fun getAllCashPager(): LiveData<PagingData<Cash>>

    suspend fun addCash(
        name: String,
        userId: Int,
        target: Long
    ): Resource<Cash>

    suspend fun updateCash(
        cashId: Int,
        name: String,
        userId: Int,
        target: Long
    ): Resource<Cash>

    suspend fun deleteCash(cashId: Int): Resource<Cash>

    suspend fun getAllTransaction(): Resource<TransactionResponse>

    suspend fun getAllTransactionByCashId(cashId: Int): Resource<TransactionResponse>

    fun getAllTransactionPager(): LiveData<PagingData<Transaction>>

    suspend fun addTransaction(
        cashId: Int,
        userId: Int,
        income: Long,
        outcome: Long,
        profit: Long,
        description: String
    ): Resource<Transaction>

    suspend fun updateTransaction(
        transactionId: Int,
        cashId: Int,
        userId: Int,
        income: Long,
        outcome: Long,
        profit: Long,
        description: String
    ): Resource<Transaction>

    suspend fun deleteTransaction(transactionId: Int): Resource<Transaction>

}