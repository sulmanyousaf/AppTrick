package com.apptrick.app.di

import com.apptrick.app.data.api.helpers.ApiNames.BASE_URL
import com.apptrick.app.data.api.service.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = if (/*BuildConfig.DEBUG*/true)
            HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)
}