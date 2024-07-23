package com.example.sampleappgpt3.data.datasource

import com.example.sampleappgpt3.data.model.CompletionPrompt
import com.example.sampleappgpt3.data.model.GPTModel
import com.example.sampleappgpt3.data.model.streaming.StreamResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface GPTDatasource {

    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    fun getCompletion(@Header("Content-Type") contentType: String,
        @Body completionPrompt: CompletionPrompt
    ): Call<GPTModel>

    @Streaming
    @POST("v1/chat/completions")
    fun getCompletionStreaming(@Header("Content-Type") contentType: String,
                      @Body completionPrompt: CompletionPrompt
    ): Call<ResponseBody>
}