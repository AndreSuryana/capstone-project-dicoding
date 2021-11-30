package com.dicoding.kasmee.data.network

import com.dicoding.kasmee.data.model.response.Wrapper
import com.dicoding.kasmee.data.model.response.auth.AuthResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}