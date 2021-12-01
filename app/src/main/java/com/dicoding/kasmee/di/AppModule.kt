package com.dicoding.kasmee.di

import android.content.Context
import com.dicoding.kasmee.BuildConfig
import com.dicoding.kasmee.data.source.remote.api.ApiService
import com.dicoding.kasmee.data.source.remote.api.Interceptor
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.data.repository.KasmeeRepositoryImpl
import com.dicoding.kasmee.data.source.remote.RemoteDataSource
import com.dicoding.kasmee.data.source.remote.RemoteDataSourceImpl
import com.dicoding.kasmee.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(
        @ApplicationContext context: Context
    ): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )

        val interceptor = Interceptor(context)

        val client = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiService: ApiService
    ) = RemoteDataSourceImpl(apiService) as RemoteDataSource

    @Provides
    @Singleton
    fun provideKasmeeRepositoryImpl(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ) = KasmeeRepositoryImpl(remoteDataSourceImpl) as KasmeeRepository
}