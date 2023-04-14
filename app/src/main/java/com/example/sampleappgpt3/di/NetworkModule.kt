package com.example.sampleappgpt3.di

import android.content.Context
import androidx.room.Room
import com.example.sampleappgpt3.BuildConfig
import com.example.sampleappgpt3.data.datasource.GPTDatasource
import com.example.sampleappgpt3.data.datastore.entity.AppDatabase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
//    @Provides
//    @Singleton
//    fun providesGPTDatasource(
//        @ApplicationContext context: Context,
//    ): GPTDatasource {
//        val GPTBaseUrl = ""
//        val okhttpCallFactory = OkHttpClient.Builder()
//            .addInterceptor(
//                HttpLoggingInterceptor()
//                    .apply {
//                        if (BuildConfig.DEBUG) {
//                            setLevel(HttpLoggingInterceptor.Level.BODY)
//                        }
//                    },
//            )
//            .build()
//        return Retrofit.Builder()
//            .baseUrl(GPTBaseUrl)
//            .callFactory(okhttpCallFactory)
//            .addConverterFactory(
////                @OptIn(ExperimentalSerializationApi::class)
////                networkJson.asConverterFactory("application/json".toMediaType()),
//            )
//            .build()
//            .create(RetrofitNiaNetworkApi::class.java)
//    }

    @Provides
    fun provideGPTDatasource(): GPTDatasource {
        val GPTBaseUrl = ""

        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(GPTBaseUrl)
            .build()
        return retrofit.create(GPTDatasource::class.java)
    }

//    @Provides
//    @Singleton
//    fun okHttpCallFactory(): Call.Factory =
}