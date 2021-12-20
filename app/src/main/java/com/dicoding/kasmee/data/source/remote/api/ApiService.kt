package com.dicoding.kasmee.data.source.remote.api

import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import com.dicoding.kasmee.data.model.response.auth.User
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.cash.CashResponse
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.data.model.response.transaction.TransactionResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Wrapper<AuthResponse>>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone_number") phoneNumber: String,
        @Field("password") password: String,
        @Field("password_confirmation") confirmPassword: String
    ): Response<Wrapper<AuthResponse>>

    @GET("user")
    suspend fun getUserInfo(): Response<Wrapper<User>>

    @FormUrlEncoded
    @PUT("user/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone_number") phoneNumber: String
    ): Response<Wrapper<User>>

    @FormUrlEncoded
    @POST("user/password")
    suspend fun changePassword(
        @Field("old_password") oldPassword: String,
        @Field("password") newPassword: String,
        @Field("confirm_password") confirmNewPassword: String
    ): Response<Wrapper<User>>

    @POST("logout")
    suspend fun logout(): Response<Wrapper<Boolean>>

    @GET("cash")
    suspend fun getAllCash(
        @Query("page") page: Int
    ): Response<Wrapper<CashResponse>>

    @GET("cash/{id}")
    suspend fun getCashById(
        @Path("id") cashId: Int
    ): Response<Wrapper<List<Cash>>>

    @GET("cash/latest")
    suspend fun getLatestCash(): Response<Wrapper<List<Cash>>>

    @FormUrlEncoded
    @POST("cash")
    suspend fun addCash(
        @Field("nama") name: String,
        @Field("target") target: Long,
    ): Response<Wrapper<Cash>>

    @FormUrlEncoded
    @POST("cash/{id}")
    suspend fun updateCash(
        @Path("id") cashId: Int,
        @Field("nama") name: String,
        @Field("target") target: Long,
    ): Response<Wrapper<Cash>>

    @DELETE("cash/{id}")
    suspend fun deleteCash(
        @Path("id") cashId: Int,
    ): Response<Wrapper<Cash>>

    @GET("transaction")
    suspend fun getAllTransaction(
        @Query("page") page: Int
    ): Response<Wrapper<TransactionResponse>>

    @GET("transaction/{id}")
    suspend fun getTransactionByCashId(
        @Path("id") cashId: Int,
        @Query("page") page: Int
    ): Response<Wrapper<TransactionResponse>>

    @GET("transaction/today/{day}")
    suspend fun getTodayTransaction(
        @Path("day") day: String
    ): Response<Wrapper<Transaction>>

    @GET("transaction/latest")
    suspend fun getLatestTransaction(): Response<Wrapper<List<Transaction>>>

    @FormUrlEncoded
    @POST("transaction")
    suspend fun addTransaction(
        @Field("id_kas") cashId: Int,
        @Field("pemasukan") income: Long,
        @Field("pengeluaran") outcome: Long,
        @Field("keuntungan") profit: Long,
        @Field("keterangan") description: String,
    ): Response<Wrapper<Transaction>>

    @FormUrlEncoded
    @POST("transaction/{id}")
    suspend fun updateTransaction(
        @Path("id") transactionId: Int,
        @Field("id_kas") cashId: Int,
        @Field("pemasukan") income: Long,
        @Field("pengeluaran") outcome: Long,
        @Field("keuntungan") profit: Long,
        @Field("keterangan") descriptin: String,
    ): Response<Wrapper<Transaction>>

    @DELETE("transaction/{id}")
    suspend fun deleteTransaction(
        @Path("id") transactionId: Int,
    ): Response<Wrapper<Transaction>>
}