package com.dicoding.kasmee.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.data.model.response.transaction.TransactionResponse
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

    override suspend fun addCash(name: String, userId: Int, target: Long): Resource<Cash> =
        remoteDataSource.addCash(name, userId, target)

    override suspend fun updateCash(
        cashId: Int,
        name: String,
        userId: Int,
        target: Long
    ): Resource<Cash> =
        remoteDataSource.updateCash(cashId, name, userId, target)

    override suspend fun deleteCash(cashId: Int): Resource<Cash> =
        remoteDataSource.deleteCash(cashId)

    override suspend fun getAllTransaction(): Resource<TransactionResponse> =
        remoteDataSource.getAllTransaction()

    override fun getAllTransactionPager(): LiveData<PagingData<Transaction>> =
        remoteDataSource.getAllTransactionPager()

    override suspend fun addTransaction(
        cashId: Int,
        userId: Int,
        income: Long,
        outcome: Long,
        profit: Long,
        description: String
    ): Resource<Transaction> =
        remoteDataSource.addTransaction(cashId, userId, income, outcome, profit, description)

    override suspend fun updateTransaction(
        transactionId: Int,
        cashId: Int,
        userId: Int,
        income: Long,
        outcome: Long,
        profit: Long,
        description: String
    ): Resource<Transaction> =
        remoteDataSource.updateTransaction(
            transactionId,
            cashId,
            userId,
            income,
            outcome,
            profit,
            description
        )

    override suspend fun deleteTransaction(transactionId: Int): Resource<Transaction> =
        remoteDataSource.deleteTransaction(transactionId)
}