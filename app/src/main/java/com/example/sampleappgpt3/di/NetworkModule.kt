package com.example.sampleappgpt3.di

import com.example.sampleappgpt3.data.AuthInterceptor
import com.example.sampleappgpt3.data.datasource.GPTDatasource
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideGPTDatasource(): GPTDatasource {
        val GPTBaseUrl = "https://api.openai.com/"
        val openAPISecretKey = "<YOUR OPENAPI KEY>"
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(timeout = 50000, TimeUnit.MILLISECONDS)
            .addInterceptor(AuthInterceptor(openAPISecretKey))
            .build()
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(GPTBaseUrl)
            .build()
        return retrofit.create(GPTDatasource::class.java)
    }
}