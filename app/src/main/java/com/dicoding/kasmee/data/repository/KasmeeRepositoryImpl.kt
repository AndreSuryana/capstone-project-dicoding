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

    override suspend fun updateProfile(
        id: Int,
        name: String,
        email: String,
        phoneNumber: String
    ): Resource<User> =
        remoteDataSource.updateProfile(id, name, email, phoneNumber)

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Resource<User> =
        remoteDataSource.changePassword(oldPassword, newPassword, confirmNewPassword)

    override suspend fun logout(): Resource<Boolean> =
        remoteDataSource.logout()

    override suspend fun getAllCash(): Resource<CashResponse> =
        remoteDataSource.getAllCash()

    override fun getAllCashPager(): LiveData<PagingData<Cash>> =
        remoteDataSource.getAllCashPager()

    override suspend fun getCashById(cashId: Int): Resource<Cash> =
        remoteDataSource.getCashById(cashId)

    override suspend fun getLatestCash(): Resource<List<Cash>> =
        remoteDataSource.getLatestCash()

    override suspend fun addCash(name: String, target: Long): Resource<Cash> =
        remoteDataSource.addCash(name, target)

    override suspend fun updateCash(
        cashId: Int,
        name: String,
        target: Long
    ): Resource<Cash> =
        remoteDataSource.updateCash(cashId, name, target)

    override suspend fun deleteCash(cashId: Int): Resource<Cash> =
        remoteDataSource.deleteCash(cashId)

    override suspend fun getAllTransaction(): Resource<TransactionResponse> =
        remoteDataSource.getAllTransaction()

    override fun getAllTransactionPager(): LiveData<PagingData<Transaction>> =
        remoteDataSource.getAllTransactionPager()

    override suspend fun getTodayTransaction(day: String): Resource<Transaction> =
        remoteDataSource.getTodayTransaction(day)

    override suspend fun getAllTransactionByCashId(cashId: Int): Resource<TransactionResponse> =
        remoteDataSource.getAllTransactionByCashId(cashId)

    override suspend fun getLatestTransaction(): Resource<List<Transaction>> =
        remoteDataSource.getLatestTransaction()

    override suspend fun addTransaction(
        cashId: Int,
        income: Long,
        outcome: Long,
        profit: Long,
        description: String
    ): Resource<Transaction> =
        remoteDataSource.addTransaction(cashId, income, outcome, profit, description)

    override suspend fun updateTransaction(
        transactionId: Int,
        cashId: Int,
        income: Long,
        outcome: Long,
        profit: Long,
        description: String
    ): Resource<Transaction> =
        remoteDataSource.updateTransaction(
            transactionId,
            cashId,
            income,
            outcome,
            profit,
            description
        )

    override suspend fun deleteTransaction(transactionId: Int): Resource<Transaction> =
        remoteDataSource.deleteTransaction(transactionId)
}