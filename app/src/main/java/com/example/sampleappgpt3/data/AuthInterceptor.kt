package com.example.sampleappgpt3.data

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(private val token: String): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val builder: Request.Builder = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
        val newRequest: Request = builder.build()
        return chain.proceed(newRequest)
    }
}